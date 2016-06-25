# aggregaTor


The aggregaTor project is part of the  Analytics Server project which aims to 
build an environment that facilitates analyzing Tor metrics data.

AggregaTors task is to provide useful aggregations of Tor metrics data:
- as code that you can run on your laptop
- as downloadable files in formats like JSON and Parquet

It works with Tor metrics data that has already been converted to JSON or 
Parquet through the converTor conversion tool (which is another part of the 
Analytics Server project).

## A little more detail

Tor provides a lot of data about network nodes through its CollecTor service. 
That data is mostly collected on a per-network node basis and serialized to a 
very specific format. There are libraries for programmatic access to this data 
in Java, Python and GO. ConverTor converts this data to JSON and Parquet to make
it easily accessible by all sorts of tools: of the shelf desktop BI tools like 
Tableau, but also the whole big data universe of the Hadoop universe.

Converting the data from the homegrown serialization to more popular formats is 
only the first step though. The data is still mostly very fine grained, one 
file per network node per hour. AggregaTor uses tools from the Hadoop family of
Big Data softwares to aggregate data over bigger timespans, over different types
of network nodes, about facts that are not readily available from the collected
data but require extra computation etc.

AggregaTor will publish aggregations ready for consumption so that not everybody
has to compute e.g. users per hour per country on his own laptop. It will also 
publish the scripts used to perform these computations so everybody can build on 
them, refine them or take them as templates for other aggregations (since 
aggregaTor will not try to provide a comprehensive offer of every sensible 
aggregation - that's just not possible). If all works out well these aggregation
scripts will form a modular toolbox of simple scripts that can be combined to 
elaborate and sophisticated pipelines. But we'll see how far we get with that...

## softwares and languages

We are currently concentrating on Spark and Drill as the softwares we develop on
and for. They both stem from the Hadoop ecosystem, but can be installed as 
standalone packages on any laptop without the need to install a whole Hadoop 
cluster. But scripts developed on the laptop can be executed on Hadoop clusters
in the cloud without any modifications. So you get both worlds in one toolset.

Spark is an engine that supports a lot of computation models. It can run classic
big data map/reduce jobs, but also has extensions for SQL execution, 
parallelized Machine Learning algorithms, Graph computation etc. It supports 
different languages as well: Java, Scala, Python and, through adapters, R and
many more. It's a very versatile environment in which we aim to try a lot of 
stuff with the data.

Drill provides only SQL but while Sparks SQL support is limited to the less
complex types of queries Drill provides full ANSI compliant SQL. It can't 
process just any data you might want to throw at it but the constraints are well 
defined and practical means to overcome them are available.

Both engines work well on data in files and directories (under the condition
that it's in a format like JSON or Parquet). So no database is needed. Of course
there are use cases where a database is beneficial and we plan to add HBase to
our tool chain, specifically useful schemata for setting it up.

As already mentioned a Hadoop cluster or even a HDFS partition is not required 
to use these tools but we'll nevertheless try to provide a sample setup, either
as a virtual machine or at least a setup guide to facilitate the move to a 
parallelized, distributed computing platform when the need arises. After all
it is one of the promises of big data technology that you can "just add a few
more machines" if your server can't handle the load anymore. It is a compelling 
perspective to be able to develop complex aggregations on a small set of sample
data and then let it run on the whole set in an on demand rented cluster instead
of blocking your local machine for a looooong time.

Language wise we will probably work with Scala and SQL most of the time but want 
to provide at least a port or two from Scala to Python and Java as examples. The
integration of existing R scripts would a useful exercise too.