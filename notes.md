
# some config on my mac to get rid of "unknown blabla" java error on spark-shell

    sudo bbedit /private/etc/hosts
    127.0.0.1	ash // add this a line after "127.0.0.1	localhost"

# pretty printing JSON on the command line

     jq (http://stedolan.github.io/jq) 
     great for pretty-printing and color highlighting JSON
     
     $ head ‐n 1 2015‐03‐01‐0.json | jq '.'

# first foray into spark land


    val conf = new SparkConf().setAppName("Spark wordcount")
    val sc = new SparkContext(conf)
    
    
    val lines = sc.textFile("hdfs://path/to/the/file")
    val oomLines = lines.filter(l => l.contains(“OutOfMemoryError”)).cache()
    val result = oomLines.count()
    
    
    val file = sc.textFile("hdfs://...")
    val counts = file
      .flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .countByKey()
    counts.saveAsTextFile("hdfs://...")
    
    
    // define named function
    def isBSD(line: String) = { line.contains("BSD") }
    // or store a reference to the function definition in a variable
    val isBSD = (line: String) => line.contains("BSD")
    // and then use it
    val bsdLines1 = licLines.filter(isBSD)
    // do something with it
    bsdLines1.count
    // print it
    bsdLines.foreach(bLine => println(bLine))
    // print it, but easier
    bsdLines.foreach(println)
    
    
    scala> val numbers = sc.parallelize(10 to 50 by 10) 
                         // makeRDD is an alias to parallelize
                         // Array, List, Range all implement Seq(uence)
    scala> numbers.foreach(x => println(x))
    scala> val numbersSquared = numbers.map(num => num * num)
    scala> numbersSquared.foreach(x => println(x))
    
    // map can also change an RDDs type
    scala> val reversed = numbersSquared.map(x => x.toString.reverse)
    scala> reversed.foreach(x => println(x))
    
    // or even shorter, with _ as a placeholder
    scala> val alsoReversed = numbersSquared.map(_.toString.reverse)
    // and some calls
    scala> alsoReversed.first
    scala> alsoReversed.top(4)  // return top 4 (sorted alphabetically)
    
    // bash
    $ echo "15,16,20,20
    77,80,94
    94,98,16,31
    31,15,20" > ~/client‐ids.log
    //spark-shell
    scala> val lines = sc.textFile("/home/spark/client‐ids.log")
    scala> val idsStr = lines.map(line => line.split(","))
    scala> idsStr.foreach(println(_))  // will print 4 java array objects :-/
    scala> idsStr.first    // prints Array[String] = Array(15, 16, 20, 20)
    scala> idsStr.collect  // collect is a an action
                               res1: Array[Array[String]] = Array(Array(15, 16, 
                               20, 20), Array(77, 80, 94), Array(94, 98, 1 ...
    scala> val ids = lines.flatMap(_.split(",")) 
                           // flatMap flattens an array of arrays into 1 array
    scala> ids.collect     // Array[String] = Array(15, 16, 20, 2 ... , 15, 20)
    scala> ids.collect.mkString("; ")  // String = 15; 16; 20; 20 ... ; 15; 20)
    scala> val intIds = ids.map(_.toInt)
    scala> intIds.collect  // Array[Int] = Array(15, 16, 20, 20,  ... , 15, 20)
    scala> val uniqueIds = intIds.distinct
    scala> uniqueIds.collect //  Array[Int] = Array(16, 80, 98, 20 ...  77, 31)
    scala> val finalCount = uniqueIds.count  // finalCount: Long = 8
    scala> val transactionCount = ids.count  // transactionCount: Long = 14
    scala> uniqueIds.take(2)                 // Array[String] = Array(80, 20)
    
    // auto conversion from Int to Double  (provide access to statistic methods)
    scala> intIds.mean                       // Double = 44.785714285714285
    scala> intIds.sum                        // Double = 627.0
    intIds.variance                          // Double = 1114.8826530612246
    scala> intIds.stdev                      // Double = 33.38985853610681
    scala> intIds.histogram(Array(1.0, 50.0, 100.0)) 
                                             // Array[Long] = Array(9, 5)
    scala> intIds.histogram(3)
              // (Array[Double], Array[Long]) = (Array(15.0, 42.66666666666667, 
              //  70.33333333333334, 98.0),Array(9, 0, 5))
              
              
              
    transformations     produce a new RDDs 
        filter
        map
        distinct
        sample
     
    actions             trigger a computation, return the result to calling code
        count
        foreach
        collect
        takeSample
        take
        stats
        histogram
        
    transformations are evaluated lazily
        computation is not started until an action is invoked
        a set of transformations that then gets executed  is called 'lineage'
        