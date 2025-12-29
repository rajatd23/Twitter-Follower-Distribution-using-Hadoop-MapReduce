# Twitter Follower Distribution using Hadoop MapReduce

## Project Overview
This project analyzes Twitter follower relationships using Apache Hadoop MapReduce to compute the distribution of users based on follower counts. It demonstrates a multi-stage MapReduce pipeline for large-scale social network data processing.

The goal of this project is to answer:
How many users have 1 follower, 2 followers, 3 followers, and so on?

## Problem Statement
Given Twitter follower data in the format:
follower,followed_user

Each record represents a follower relationship where one user follows another user. The objective is to:
1. Calculate the total number of followers for each user
2. Group users based on follower count to generate a follower distribution

## Solution Approach
The solution is implemented using two chained MapReduce jobs.

Job 1: Count Followers per User
- Mapper emits (followed_user, 1)
- Reducer aggregates the total number of followers per user

Intermediate Output:
userA    5
userB    12
userC    1

Job 2: Group Users by Follower Count
- Mapper emits (follower_count, 1)
- Reducer counts how many users have the same follower count

Final Output:
1     3421
2     1298
5     412

## Input Format
CSV file stored in HDFS:
userA,userB
userC,userB
userD,userA

## Output Format
<follower_count>    <number_of_users>

Example:
2    1200
5    300

## Technologies Used
- Java
- Apache Hadoop MapReduce
- HDFS

## How to Run

Compile:
javac -classpath `hadoop classpath` -d twitter_classes Twitter.java
jar -cvf twitter.jar -C twitter_classes/ .

Execute:
hadoop jar twitter.jar Twitter input_path temp_output_path final_output_path

## Use Cases
- Social network analysis
- Popularity distribution modeling
- Big data analytics pipelines
- Graph data preprocessing

## Key Highlights
- Multi-stage MapReduce job chaining
- Scalable follower aggregation
- Efficient grouping and counting
- Real-world social network data use case

## Possible Enhancements
- Add Combiner for optimization
- Identify top-K most followed users
- Implement Apache Spark version
- Visualize follower distribution
- Deploy on AWS EMR

## License
Open-source project for educational and research purposes.
