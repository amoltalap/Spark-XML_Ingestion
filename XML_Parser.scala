import scala.xml._

def parseMyXML(xml:String) = {
        val myXml = scala.xml.XML.loadString(xml)
        val comments_comment = (myXml \ "Comments" \ "Comment")
//		val data = comments_comment.map{x=>(x.child(0).text
//											,x.child(1).text)}
		val data = comments_comment.map{x=>(if(x.child(0).label == "Title")
												x.child(0).text
											,if(x.child(1).label == "Description")
												x.child(1).text
											,(x \ "_").length)	}
		//((XML \ "c")(0) \ "_").length
		var json_data:String =""
		for(comment <- comments_comment)
		{
			json_data += "{"
			var title = (comment \\ "Title").text
			var descr = (comment \\ "Description").text
			var fdescr = (comment \\ "FullDescription").text
			json_data += "{ Title:" + title + ", Description:" + descr + ", FullDescription:" + fdescr +"}"
			
			for(author <- (comment \ "Authors" \ "Author"))
			{
				val author_name = (author \\ "Name").text
				val author_dofw = (author \\ "Date_of_Writing").text
				val author_misstag = (author \\ "MissingTag").text
				
				json_data += "{ Author_name:" + author_name + ", Author_Date_of_Writing:" + author_dofw + ", Author_MissingTag:" + author_misstag +"}"			
				
			}
			for(publisher <- (comment \ "Publishers" \ "Publisher"))
			{
				val publisher_name = (publisher \\ "Name").text
				val publisher_dofw = (publisher \\ "Date_of_Writing").text
				val publisher_misstag = (publisher \\ "MissingTag").text
				
				json_data += "{ Publisher_name:" + publisher_name + ", Publisher_Date_of_Writing:" + publisher_dofw + ", Publisher_MissingTag:" + publisher_misstag +"}"			
			}
			json_data += "}"			
		}
		println(json_data)
		data
}

/**def readXML(x:scala.xml.NodeSeq) {
	//for (n <- nodes) println(n.getClass)
	(x.child(0).text,x.child(1).text)
}*/

val xml1="<books><Comments><Comment><Title>Title1.1</Title><Description>Descript1.1</Description><Authors><Author><Name>Suresh</Name><Date_of_Writing>2017-01-11 </Date_of_Writing></Author><Author><Name>Amol</Name><Date_of_Writing>2017-07-17 </Date_of_Writing></Author></Authors><Publishers><Publisher><Name>Vishal</Name><Date_of_Publish>2016-01-11 </Date_of_Publish></Publisher><Publisher><Name>Arindam</Name><Date_of_Publish>2015-01-11 </Date_of_Publish></Publisher></Publishers></Comment></Comments></books>"

val list = parseMyXML(xml1)
list.foreach(println)