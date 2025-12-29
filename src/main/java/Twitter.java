import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import java.io.IOException;
import java.util.Iterator;

class MapOne extends Mapper<Object, Text, Text, IntWritable> {
    private Text Uid = new Text();
    private final static IntWritable newOne = new IntWritable(1);

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] list = value.toString().split(",");
        if (list.length == 2) {
            Uid.set(list[1].trim());
            context.write(Uid, newOne);
        }
    }
}

class ReduceOne extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable toreturnresult = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int totalNumber = 0;
        Iterator<IntWritable> iterator = values.iterator();
        while (iterator.hasNext()) {
            totalNumber += iterator.next().get();
        }
        toreturnresult.set(totalNumber);
        context.write(key, toreturnresult);
    }
}

class MapTwo extends Mapper<Object, Text, IntWritable, IntWritable> {
    private IntWritable followercount = new IntWritable();
    private final static IntWritable newOne = new IntWritable(1);

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] list = value.toString().split("\t");
        if (list.length == 2) {
            int count = Integer.parseInt(list[1].trim());
            followercount.set(count);
            context.write(followercount, newOne);
        }
    }
}

class ReduceTwo extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    private IntWritable toreturnresult = new IntWritable();

    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int totalNumber = 0;
        Iterator<IntWritable> iterator = values.iterator();
        while (iterator.hasNext()) {
            totalNumber += iterator.next().get();
        }
        toreturnresult.set(totalNumber);
        context.write(key, toreturnresult);
    }
}


public class Twitter {

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        Job firstjob = Job.getInstance(config, "count followers");
        firstjob.setJarByClass(Twitter.class);
        firstjob.setMapperClass(MapOne.class);
        firstjob.setReducerClass(ReduceOne.class);
        firstjob.setOutputKeyClass(Text.class);
        firstjob.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(firstjob, new Path(args[0]));
        Path temporaryoutputPath = new Path(args[1]);
        FileOutputFormat.setOutputPath(firstjob, temporaryoutputPath);

        if (!firstjob.waitForCompletion(true)) {
            System.exit(1);
        }
        Job secondJob = Job.getInstance(config, "group by follower count");
        secondJob.setJarByClass(Twitter.class);
        secondJob.setMapperClass(MapTwo.class);
        secondJob.setReducerClass(ReduceTwo.class);
        secondJob.setOutputKeyClass(IntWritable.class);
        secondJob.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(secondJob, temporaryoutputPath);
        FileOutputFormat.setOutputPath(secondJob, new Path(args[2]));
        System.exit(secondJob.waitForCompletion(true) ? 0 : 1);
    }
}
