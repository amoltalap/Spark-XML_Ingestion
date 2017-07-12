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
		
		var data_map = Map[String, Any]()
		var data_list = new ListBuffer[Any]()
		
		for(comment <- comments_comment)
		{
			var title = (comment \\ "Title").text.trim
			var descr = (comment \\ "Description").text.trim
			var fdescr = (comment \\ "Full_Description").text.trim	//data missing tag
			
			var comment_list = new ListBuffer[Comment]()
			comment_list += Comment(title,descr,fdescr)
			data_map.put("comments", comment_list.toList)
			data_list += comment_list.toList
			
			var child1 = (comment \ "Authors" \ "Author")
			var author_list = new ListBuffer[Author]()
			for(author <- child1)
			{
				val author_name = (author \\ "Name").text.trim
				val author_date = (author \\ "Date_of_Writing").text.trim
				val author_misstag = (author \\ "MissingTag").text.trim
				author_list += Author(author_name,author_date,author_misstag)
			}
			println("No. of nodes " +  (child1 \ "_").length)
			//Need to change this validation lengths to make fool proof
			if((child1 \ "_").length > 0 && author_list.length > 0) {
				data_map.put("authors",author_list.toList)
			}
			data_list += author_list.toList
			
			var child2 = (comment \ "Publishers" \ "Publisher")
			var publisher_list = new ListBuffer[Publisher]()

			for(publisher <- child2)
			{
				val publisher_name = (publisher \\ "Name").text.trim
				val publisher_date = (publisher \\ "Date_of_Publish").text.trim
				val publisher_misstag = (publisher \\ "MissingTag").text.trim
				publisher_list += Publisher(publisher_name,publisher_date,publisher_misstag)
			}
			data_map.put("publishers",publisher_list.toList)
			data_list += publisher_list.toList
			println("")
			println("data_map is " + data_map)
			println("")
			println("data_list is " + data_list.toList)
			println("")
		}
		//data_map
		data_list.toList
}

val xml1="<books><Comments><Comment><Title>Title1.1</Title><Description>Descript1.1</Description><Authors><Author><Name>Suresh</Name><Date_of_Writing>2017-01-11 </Date_of_Writing></Author><Author><Name>Amol</Name><Date_of_Writing>2017-07-17 </Date_of_Writing></Author></Authors><Publishers><Publisher><Name>Vishal</Name><Date_of_Publish>2016-01-11 </Date_of_Publish></Publisher><Publisher><Name>Arindam</Name><Date_of_Publish>2015-01-11 </Date_of_Publish></Publisher></Publishers></Comment></Comments></books>"

val list = parseMyXML(xml1)
println("")
list.foreach(println)
