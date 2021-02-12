/* Author: Skander Marnissi */

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.Edge
import org.apache.spark.graphx.Graph

object main_td6 extends App {
  val conf = new SparkConf().setAppName("simpleSparkApp").setMaster("local")
  val sc = new SparkContext(conf)
  sc.setLogLevel("FATAL")

  val directory = "Cloud-computing-mapreduce/td/td6/"
  val drugs = sc.textFile(directory + "med.txt").map(x => x.split("\t")).map(x => (x(0).toLong, (x(1), x(6))))

  //Print test:
  //println(drugs.count)

  val status = drugs.map(x => (x._2._2, 1)).reduceByKey(_+_)

  //Print test:
  //status.take(10).foreach(println)

  val comDrugs = drugs.filter{case(cis, (nom, etat)) => !etat.contains("Non")}.map{case(cis, (nom, etat)) => (cis, nom)}

  //Print test:
  //println(comDrugs.count)

  val sub = sc.textFile(directory + "compo.txt").map(x => x.split("\t")).map(x => (x(0).toLong, (x(2).toLong, x(3), x(6))))

  //Print test:
  //println(sub.count)

  val tmp0 = sub.map{case(cis, (cs, ns, na)) => (na, 1)}.reduceByKey(_+_)

  //Print test:
  //tmp0.foreach(println)

  val saSub = sub.filter{case(cis, (cs, ns, na)) => na == "SA"}.map{case(cis, (cs, ns, na)) => (cis, (cs, ns))}

  //Print test:
  //println(saSub.count)

  val edges0 = saSub.map{case(cis, (code, nom)) => ((cis, code), 1)}.reduceByKey(_+_)

  //Print test:
  //println(edges0.count)

  val tmp = edges0.filter{case((ci, cs), n) => n > 1}

  //Print test:
  //println(tmp.count)

  val edges1 = saSub.map{case(cis, (code, nom)) => Edge(cis.toLong, code.toLong)}.distinct

  //Print test:
  //println(edges1.count)

  val tmp2 = tmp.map{case((i, s), n) => n}

  //Print test:
  //tmp2.take(10).foreach(println)

  val saSubV = saSub.map{case(cis, (code, nom)) => (code, nom)}.distinct

  //Print test:
  //println(saSubV.count)

  val vertices = comDrugs.union(saSubV)

  //Print test:
  //println(vertices.count)

  val edges = saSub.map{case(cis, (code, nom)) => Edge(cis.toLong, code.toLong, null)}.distinct
  val drugGraph = Graph(vertices, edges)

  //Print test:
  //println(drugGraph.vertices.count)
  //println(drugGraph.edges.count)

  val tolerance = 0.0001
  val ranking = drugGraph.pageRank(tolerance).vertices

  //Print test:
  //ranking.collect.foreach(println)

  val r2 = ranking.map{case(id, va) => (va, id)}

  //Print test:
  //r2.top(10).foreach(println)

  val rankSub = saSubV.join(ranking).map{case(cs, (ns, ra)) => (cs, (ns.toLowerCase(), ra))}

  //Print test:
  //println(rankSub.count)

  val rank12 = rankSub.filter{case(cs, (ns, ra)) => ra.toDouble >= 12}

  //Print test:
  //println(rank12.count)

  val tCount = drugGraph.triangleCount().vertices.filter{case(id, co) => co > 0}

  //Print test:
  //println(tCount.count)

  val saSub2 = sub.filter{case(cis, (cs, ns, na)) => na == "SA"}.map{case(cis, (cs, ns, na)) => (cis, cs)}

  //Print test:
  //println(saSub2.count)

  val m = saSub2.distinct.groupByKey()
  val m2 = m.filter(x => x._2.size > 1)
  val m3 = m2.map(x => x._2.toList)
  val m4 = m3.map(x => x.combinations(2).toList).flatMap(x => x)
  val m4Edge = m4.map(x => Edge(x(0), x(1), null))
  val m5 = m4.flatMap(x => x).distinct.map(x => (x, null)).join(saSub.map(x => (x._2._1, x._2._2)).distinct).map(x => (x._1, x._2._2))
  val subGraph = Graph(m5, m4Edge)

  //Print test:
  //println(subGraph.vertices.count)
  //println(subGraph.edges.count)

  val coc = subGraph.connectedComponents

  val t = coc.triplets
  val t2 = t.filter(x => x.dstId == 3671 || x.srcId == 3671)

  //Print test:
  //t2.collect.foreach(println)

}
