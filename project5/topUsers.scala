val fileRdd = sc.textFile( "twitter.edges" )
val userRdd = fileRdd.map( line => line.split(": ") ).flatMap( arr =>
	arr( 1 ).split( "," ).map( user => ( user, 1 ) ) )
val userCounts = userRdd.reduceByKey( ( a, b ) => a + b )
val finalResults = userCounts.filter( user => user._2 > 1000)
finalResults.saveAsTextFile("output")
System.exit(0)