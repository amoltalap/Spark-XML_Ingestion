def parseMyXML(xml:String) = {
        val myXml = scala.xml.XML.loadString(xml)
        val comments_comment = (myXml \ "Comments" \
"Comment").map(x=>(x.child(0).text,x.child(1).text))
        comments_comment
}

/*
println(">>>>>>>START UnitTest for xmlParse")
val xml1="<books><Comments><Comment><Title>Title1.1</Title><Description>Descript1.1</Description></Comment><Comment><Title>Title1.2</Title><Description>Descript1.2</Description></Comment><Comment><Title>Title1.3</Title><Description>Descript1.3</Description></Comment></Comments></books>"
val list = parseMyXML(xml1)
list.foreach(println)
println("<<<<<<<<END UnitTest for xmlParse")
*/


case class TableData(ID_col1:Int,Col2:String,Col3:String,XML_Col4:String)
val data = sc.textFile("file:///home/spark/XML_Project/data.txt")
val rdd = data.map(x=>TableData(x.split(',')(0).toInt,
x.split(',')(1), x.split(',')(2), x.split(',')(3)))

val xml_parsed_rdd =
rdd.map(x=>(x.ID_col1,x.Col2,x.Col3,parseMyXML(x.XML_Col4)))
val flattened_rdd = xml_parsed_rdd.flatMap{case(col1,col2,col3,col4)
=> col4.map(v=>(col1,col2,col3)->v)}
val flattened_map = flattened_rdd.map{case(col1,col2)
=>(col1._1,col1._2,col1._3,col2._1,col2._2)}
val df = flattened_map.toDF
df.show

