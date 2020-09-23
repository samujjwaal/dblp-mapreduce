# Homework 2
### Description: you will gain experience with a distributed computational problem. The main textbook group will design and implement an instance of the map/reduce computational model whereas the alternative textbook group will use the CORBA model.
### Grade: 10% + bonus up to 3% for deploying your map/reduce program at [AWS EMR](https://aws.amazon.com/emr) or the EC2 instances for the alternative textbook group.
#### You can obtain this Git repo using the command git clone git@bitbucket.org:cs441_fall2020/homework2.git. You cannot push your code into this repo, otherwise, your grade for this homework will be ZERO!

## Preliminaries
If you have not already done so as part of your first homework, you must create your account at [BitBucket](https://bitbucket.org/), a Git repo management system. It is imperative that you use your UIC email account that has the extension @uic.edu. Once you create an account with your UIC address, BibBucket will assign you an academic status that allows you to create private repos. Bitbucket users with free accounts cannot create private repos, which are essential for submitting your homeworks and the course project. Your instructor created a team for this class named [cs441_Fall2020](https://bitbucket.org/cs441_fall2020/). Please contact your TA, Mr.Mohanty from your **UIC.EDU** account and they will add you to the team repo as developers, since they already have the admin privileges. Please use your emails from the class registration roster to add you to the team and you will receive an invitation from BitBucket to join the team. Since it is still a large class, please use your UIC email address for communications or Piazza and avoid emails from other accounts like funnybunny1992@gmail.com. If you don't receive a response within 12 hours, please contact us via Piazza, it may be a case that your direct emails went to the spam folder.

For the main textbook student group, in case you have not done so, you will install [IntelliJ](https://www.jetbrains.com/student/) with your academic license, the JDK, the Scala runtime and the IntelliJ Scala plugin, the [Simple Build Toolkit (SBT)](https://www.scala-sbt.org/1.x/docs/index.html) and make sure that you can create, compile, and run Java and Scala programs. Please make sure that you can run [Java monitoring tools](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr025.html) or you can choose a newer JDK and tools if you want to use a more recent one.


Please set up your account with [AWS Educate](https://aws.amazon.com/education/awseducate/). Using your UIC email address will enable you to receive free credits for running your jobs in the cloud. Preferably, you should create your developer account for $30 per month to enjoy the full range of AWS services.

You will use logging and configuration management frameworks. You will comment your code extensively and supply logging statements at different logging levels (e.g., TRACE, INFO, ERROR) to record information at some salient points in the executions of your programs. All input and configuration variables must be supplied through configuration files -- hardcoding these values in the source code is prohibited and will be punished by taking a large percentage of points from your total grade! You are expected to use [Logback](https://logback.qos.ch/) and [SLFL4J](https://www.slf4j.org/) for logging and [Typesafe Conguration Library](https://github.com/lightbend/config) for managing configuration files. These and other libraries should be exported into your project using your script [build.sbt](https://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html). Alternatively, you can use Python or C++ libraries for CORBA. These libraries and frameworks are widely used in the industry, so learning them is the time well spent to improve your resume.

## Overview
In this homework, you will create a distributed program for parallel processing of the [publically available DBLP dataset](https://dblp.uni-trier.de) that contains entries for various publications at many different venues (e.g., conferences and journals). Raw [XML-based DBLP dataset](https://dblp.uni-trier.de/xml) is also publically available along with its schema and the documentation.

Each entry in the dataset describes a publication, which contains the list of authors, the title, and the publication venue and a few other attributes. The file is approximately 2.5Gb - not big by today's standards, but large enough for this homework assignment. Each entry is independent from the other one in that it can be processed without synchronizing with processing some other entries.

Consider the following entry in the dataset.
```xml
<inproceedings mdate="2017-05-24" key="conf/icst/GrechanikHB13">
<author>Mark Grechanik</author>
<author>B. M. Mainul Hossain</author>
<author>Ugo Buy</author>
<title>Testing Database-Centric Applications for Causes of Database Deadlocks.</title>
<pages>174-183</pages>
<year>2013</year>
<booktitle>ICST</booktitle>
<ee>https://doi.org/10.1109/ICST.2013.19</ee>
<ee>http://doi.ieeecomputersociety.org/10.1109/ICST.2013.19</ee>
<crossref>conf/icst/2013</crossref>
<url>db/conf/icst/icst2013.html#GrechanikHB13</url>
</inproceedings>
```

This entry lists a paper at the IEEE International Conference on Software Testing, Verification and Validation (ICST) published in 2013 whose authors are my former Ph.D. student at UIC, now tenured Associate Professor at the University of Dhaka, Prof. Dr. B.M. Mainul Hussain whose advisor Prof.Mark Grechanik is a co-author on this paper. The third co-author is Prof.Ugo Buy, a faculty member at our CS department. The presence of three co-authors in a single publication like this one increments a count variable that represents the number of publications with three co-authors. Your job is to determine the distribution of the number of authors across many different journals and conferences using the information extracted from this dataset. Paritioning this dataset into shards is easy, since it requires to preserve the well-formedness of XML only. Most likely, you will write a simple program to partition the dataset into an approximately equal size shards.

As before, this homework script is written using a retroscripting technique, in which the homework outlines are generally and loosely drawn, and the individual students improvise to create the implementation that fits their refined objectives. In doing so, students are expected to stay within the basic requirements of the homework and they are free to experiments. Asking questions is important, so please ask away at Piazza!

## Functionality
Your homework assignment is to create a program for parallel distributed processing of the publication dataset. Your goal is to produce the following statistics about the authors and the venues they published their papers at. First, you will compute a spreadsheet or an CSV file that shows top ten published authors at each venue. Second, you will compute the list of authors who published without interruption for N years where 10 <= N. Then, for each venue you will produce the list of publications that contains only one author. Next, you will produce the list of publications for each venue that contain the highest number of authors for each of these venues. Finally, you will produce the list of top 100 authors in the descending order who publish with most co-authors and the list of 100 authors who publish without any co-authors. 

### Assignment for the main textbook group
Your job is to create the mapper and the reducer for each task, explain how they work, and then to implement them and run on the DBLP dataset. The output of your map/reduce is a spreadsheet or an CSV file with the required statistics. The explanation of the map/reduce model is given in the main textbook in Chapter 4.

You will create and run your software application using [Apache Hadoop](http://hadoop.apache.org/), a framework for distributed processing of large data sets across multiple computers (or even on a single node) using the map/reduce model. If your laptop/workstation is limited in its RAM, you can use [Cloudera QuickStart VM with the minimum req of RAM 4Gb](https://www.cloudera.com/downloads/quickstart_vms/5-12.html). Even though you can install and configure Hadoop on your computers, I recommend that you use a virtual machine (VM) of [Hortonworks Sandbox](http://hortonworks.com/products/sandbox/), a preconfigured Apache Hadoop installation with a comprehensive software stack. To run the VM, you can install vmWare or VirtualBox. As UIC students, you have access to free vmWare licenses, go to http://go.uic.edu/csvmware to obtain your free license. In some cases, I may have to provide your email addresses to a department administrator to enable your free VM academic licenses. Please notify me if you cannot register and gain access to the webstore.

The steps for obtaining your free academic vmWare licenses are the following:
- Contact [Mr.Phil Bertran](pbeltr1@uic.edu) and CC to [DrMark](drmark@uic.edu) to obtain access to the vmWare academic program.
- One approved, go to [Onthehub vmWare](http://go.uic.edu/csvmware).
- Click on the "sign in" link at the top.
- Click on "register".
- Select "An account has been created..." and continue with the registration.
- Make sure that you use the UIC email with which you are registered with the system.

Only UIC students who are registered for this course are eligible. If you are auditing the course, you need to contact the uic webstore directly. Alternatively, you can use [VirtualBox from Oracle Corp](https://www.virtualbox.org/).

You can complete this homework using Scala and __you will immensely enjoy__ the embedded XML processing facilities that come with Scala. You will use Simple Build Tools (SBT) for building the project and running automated tests. I recommend that you run the downloaded VM locally in vmWare or VirtualBox to develop and test your program before you move it to AWS.

Next, after creating and testing your map/reduce program locally, you will deploy it and run it on the Amazon Elastic MapReduce (EMR) - you can find plenty of [documentation online](http://docs.aws.amazon.com/emr/latest/ManagementGuide/emr-work-with-steps.html). You will produce a short movie that documents all steps of the deployment and execution of your program with your narration and you will upload this movie to [youtube](www.youtube.com) and you will submit a link to your movie as part of your submission in the README.md file. To produce a movie, you may use an academic version of [Camtasia](https://shop.techsmith.com/store/techsm/en_US/cat/categoryID.67158100) or some other cheap/free screen capture technology from the UIC webstore or an application for a movie capture of your choice. The captured web browser content should show your login name in the upper right corner of the AWS application and you should introduce yourself in the beginning of the movie speaking into the camera.

### Assignment for the alternative textbook group
Your job is to create the distributed objects using [omniOrb CORBA framework](http://omniorb.sourceforge.net/omni42/omniORB/) for each task, explain how they work, and then to implement them and run on the DBLP dataset. The output of your distributed system is a spreadsheet or an CSV file with the required statistics. The explanation of the CORBA is given in the alternative textbook in Chapter 7 -Guide to Reliable Distributed Systems: Building High-Assurance Applications and Cloud-Hosted Services 2012th Edition by Kenneth P. Birman. You can complete your implementation using C++ or Python.

Next, after creating and testing your program locally, you will deploy it and run it on the AWS EC2 IaaS. You will produce a short movie that documents all steps of the deployment and execution of your program with your narration and you will upload this movie to [youtube](www.youtube.com) and you will submit a link to your movie as part of your submission in the README.md file. To produce a movie, you may use an academic version of [Camtasia](https://shop.techsmith.com/store/techsm/en_US/cat/categoryID.67158100) or some other cheap/free screen capture technology from the UIC webstore or an application for a movie capture of your choice. The captured web browser content should show your login name in the upper right corner of the AWS application and you should introduce yourself in the beginning of the movie speaking into the camera.


## Baseline Submission
Your baseline project submission should include your implementation, a conceptual explanation in the document or in the comments in the source code of how your mapper and reducer work to solve the problem, and the documentation that describe the build and runtime process, to be considered for grading. Your project submission should include all your source code written in Scala as well as non-code artifacts (e.g., configuration files), your project should be buildable using the SBT, and your documentation must specify how you paritioned the data and what input/outputs are. Simply copying Java programs from examples at the DBLP website and modifying them a bit will result in rejecting your submission.

## Piazza collaboration
You can post questions and replies, statements, comments, discussion, etc. on Piazza. For this homework, feel free to share your ideas, mistakes, code fragments, commands from scripts, and some of your technical solutions with the rest of the class, and you can ask and advise others using Piazza on where resources and sample programs can be found on the internet, how to resolve dependencies and configuration issues. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw2 to ensure that all discussion threads can be easily located. Active participants and problem solvers will receive bonuses from the big brother :-) who is watching your exchanges on Piazza (i.e., your class instructor). However, *you must not describe your map/reduce design or specific details related how your construct your map/reduce pipeline!*

## Git logistics
**This is an individual homework.** Separate repositories will be created for each of your homeworks and for the course project. You will find a corresponding entry for this homework at git@bitbucket.org:cs441_fall2020/homework2.git. You will fork this repository and your fork will be private, no one else besides you, the TA and your course instructor will have access to your fork. Please remember to grant a read access to your repository to your TA and your instructor. In future, for the team homeworks and the course project, you should grant the write access to your forkmates, but NOT for this homework. You can commit and push your code as many times as you want. Your code will not be visible and it should not be visible to other students (except for your forkmates for a team project, but not for this homework). When you push the code into the remote repo, your instructor and the TA will see your code in your separate private fork. Making your fork public, pushing your code into the main repo, or inviting other students to join your fork for an individual homework will result in losing your grade. For grading, only the latest push timed before the deadline will be considered. **If you push after the deadline, your grade for the homework will be zero**. For more information about using the Git and Bitbucket specifically, please use this [link as the starting point](https://confluence.atlassian.com/bitbucket/bitbucket-cloud-documentation-home-221448814.html). For those of you who struggle with the Git, I recommend a book by Ryan Hodson on Ry's Git Tutorial. The other book called Pro Git is written by Scott Chacon and Ben Straub and published by Apress and it is [freely available](https://git-scm.com/book/en/v2/). There are multiple videos on youtube that go into details of the Git organization and use.

Please follow this naming convention while submitting your work : "Firstname_Lastname_hw2" without quotes, where you specify your first and last names **exactly as you are registered with the University system**, so that we can easily recognize your submission. I repeat, make sure that you will give both your TA and the course instructor the read access to your *private forked repository*.

## Discussions and submission
You can post questions and replies, statements, comments, discussion, etc. on Piazza. Remember that you cannot share your code and your solutions privately, but you can ask and advise others using Piazza and StackOverflow or some other developer networks where resources and sample programs can be found on the Internet, how to resolve dependencies and configuration issues. Yet, your implementation should be your own and you cannot share it. Alternatively, you cannot copy and paste someone else's implementation and put your name on it. Your submissions will be checked for plagiarism. **Copying code from your classmates or from some sites on the Internet will result in severe academic penalties up to the termination of your enrollment in the University**. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw1 to ensure that all discussion threads can be easily located.


## Submission deadline and logistics
Saturday, October 17 at 11PM CST via the bitbucket repository. Your submission will include the code for your program, your documentation with instructions and detailed explanations on how to assemble and deploy your program along with the results of its run and what the limitations of your implementation are. Again, do not forget, please make sure that you will give both your TA and your instructor the read access to your private forked repository. Your name should be shown in your README.md file and other documents. Your code should compile and run from the command line using the commands **sbt clean compile test** and **sbt clean compile run** or some other build/run system like cmake. Also, you project should be IntelliJ or PyCharm or CLion friendly, i.e., your graders should be able to import your code into IntelliJ and run from there. Use .gitignore to exlude files that should not be pushed into the repo.


## Evaluation criteria
- the maximum grade for this homework is 10% with the bonus up to 3% for doing the AWS EMR part. Points are subtracted from this maximum grade: for example, saying that 2% is lost if some requirement is not completed means that the resulting grade will be 10%-2% => 8%; if the core homework functionality does not work, no bonus points will be given;
- the code does not work in that it does not produce a correct output or crashes: up to 5% lost;
- having less than five unit and/or integration tests: up to 4% lost;
- missing comments and explanations from the program: up to 5% lost;
- logging is not used in the program: up to 3% lost;
- hardcoding the input values in the source code instead of using the suggested configuration libraries: up to 4% lost;
- no instructions in README.md on how to install and run your program: up to 10% lost;
- the documentation exists but it is insufficient to understand how you assembled and deployed all components of the cloud: up to 4% lost;
- the minimum grade for this homework cannot be less than zero.

That's it, folks!
