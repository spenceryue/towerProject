# towerProject

AUTHOR: SPENCER YUE<br><br>


The objective of this project is to organize 10,000 randomly weighed and
colored blocks into the tallest tower possible while observing 2 rules:<br>

(1) A block must be lighter than another block it stacks on.<br>
(2) Block faces can only meet if they are the same color.<br>

One solution to this problem has been recorded in the file
"Blocks/sample program output (2015/08/29).txt".<br>

There are four files in the source folder (Blocks/src/blocks/) of this project:<br>
(1) Builder_v1_obsolete.java<br>
(2) Builder_v2.java<br>
(3) Block.java<br>
(4) TimeReporter.java<br>

File (1) is the first version of the algorithm. It is able to solve the problem
using 10% of the available blocks.<br>

File (2) is the second version. It is able to produce a solution using 70% of the
available blocks in just 3 seconds.<br>

The key to this problem was recognizing that it is an adaptation of the
"Longest Increasing Subsequence" problem.
