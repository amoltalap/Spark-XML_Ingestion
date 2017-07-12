import scala.xml._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable._

def parseMyXML(xml:String) = {
        val myXml = scala.xml.XML.loadString(xml)
        val comments_comment = (myXml \ "Comments" \ "Comment")
		val data = comments_comment.map{x=>(x.child(0).text
											,x.child(1).text)}
		//val data = comments_comment.map{x=>readXML (x)}
		case class Comment(title:String,description:String,full_description:String)
		case class Author(author_name:String,author_date:String,author_misstag:String)
		case class Publisher(publisher_name:String,publisher_date:String,publisher_misstag:String)
		
		var data_map = scala.collection.mutable.Map[String, Any]()

		var json_data:String =""
		for(comment <- comments_comment)
		{
			json_data += "{"
			var title = (comment \\ "Title").text
			var descr = (comment \\ "Description").text
			var fdescr = (comment \\ "Full_Description").text	//data missing tag
			json_data += "{ Title:" + title + ", Description:" + descr + ", Full_Description:" + fdescr +"}"
			
			var comment_list = new ListBuffer[Comment]()
			comment_list += Comment(title,descr,fdescr)
			println(comment_list.toList)
			//data_map("comments" ->comment_list)
			
			var child1 = (comment \ "Authors" \ "Author")
			//println(child1 \ "_".length)
			var author_list = new ListBuffer[Author]()
			json_data += " Authors : ["
			for(author <- child1)
			{
				val author_name = (author \\ "Name").text
				val author_date = (author \\ "Date_of_Writing").text
				val author_misstag = (author \\ "MissingTag").text
				author_list += Author(author_name,author_date,author_misstag)
				
				json_data += " Author :{ Author_name:" + author_name + ", Author_Date_of_Writing:" + author_date + ", Author_MissingTag:" + author_misstag +"} , "			
			}
			println(author_list.toList)
			json_data += "]"
			
			var child2 = (comment \ "Publishers" \ "Publisher")
			//println(child2 \ "_".length)
			var publisher_list = new ListBuffer[Publisher]()
			json_data += " Publishers : ["
			for(publisher <- child2)
			{
				val publisher_name = (publisher \\ "Name").text
				val publisher_date = (publisher \\ "Date_of_Publish").text
				val publisher_misstag = (publisher \\ "MissingTag").text
				publisher_list += Publisher(publisher_name,publisher_date,publisher_misstag)
				
				json_data += " Publisher :{ Publisher_name:" + publisher_name + ", Date_of_Publish:" + publisher_date + ", Publisher_MissingTag:" + publisher_misstag +"} , "			
			}
			println(publisher_list.toList)
			
			json_data += "]"
			json_data += "}"			
		}
		println(json_data)
		data
}

/*
def readXML(nodes:scala.xml.NodeSeq) {
	var myNodes = new ListBuffer[String]()
	println("No. of nodes " + ((nodes \ "_").length))
	for(i <- 0 until ((nodes \ "_").length) ) {
		if(nodes.child(i).label == "Title")
			//myNodes += nodes.child(i).text
			myNodes += i.toString
	}		
	myNodes
	/**
	(x.child(0).text,x.child(1).text)
	if(x.child(0).label == "Title")
												x.child(0).text
											,if(x.child(1).label == "Description")
												x.child(1).text
	var nn:String 
	*/
}
*/
val xml1="<books><Comments><Comment><Title>Title1.1</Title><Description>Descript1.1</Description><Authors><Author><Name>Suresh</Name><Date_of_Writing>2017-01-11 </Date_of_Writing></Author><Author><Name>Amol</Name><Date_of_Writing>2017-07-17 </Date_of_Writing></Author></Authors><Publishers><Publisher><Name>Vishal</Name><Date_of_Publish>2016-01-11 </Date_of_Publish></Publisher><Publisher><Name>Arindam</Name><Date_of_Publish>2015-01-11 </Date_of_Publish></Publisher></Publishers></Comment></Comments></books>"
/** 
{{ "Title":"Title1.1", "Description":"Descript1.1", FullDescription:""}, Authors: [ Author :{ Author_name:Suresh, Author_Date_of_Writing:2017-01-11 , Author_MissingTag:}, Author :{ Author_name:Amol, Author_Date_of_Writing:2017-07-17 , Author_MissingTag:}], Publishers: [Publisher: { Publisher_name:Vishal, Publisher_Date_of_Writing:, Publisher_MissingTag:} ,Publisher: { Publisher_name:Arindam, Publisher_Date_of_Writing:, Publisher_MissingTag:""}]}*/

val list = parseMyXML(xml1)
list.foreach(println)