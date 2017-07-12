
import scala.xml._
import scala.collection.mutable.ListBuffer

case class Comment(title: String, description: String, full_description: String)
case class Author(author_name: String, author_date: String, author_misstag: String)
case class Publisher(publisher_name: String, publisher_date: String, publisher_misstag: String)

case class TitleRow(comment: Comment, author: Author, publisher: Publisher)

//val requirement = "original"
val requirement = "new"

def parseMyXML(xml: String, requirement: String) : List[TitleRow] = {

  val myXml = scala.xml.XML.loadString(xml)
  val comments_comment = (myXml \ "Comments" \ "Comment")

  var titleList = new ListBuffer[TitleRow]

  for (comment <- comments_comment) {
    var title = (comment \\ "Title").text.trim
    var descr = (comment \\ "Description").text.trim
    var fdescr = (comment \\ "Full_Description").text.trim //data missing tag

    val currComment = Comment(title, descr, fdescr)

    val authorList: Seq[Author] = (comment \ "Authors" \ "Author").map(
      author => Author(
        (author \\ "Name").text.trim,
        (author \\ "Date_of_Writing").text.trim,
        (author \\ "MissingTag").text.trim
      )
    )

    val publisherList: Seq[Publisher] = (comment \ "Publishers" \ "Publisher").map(
      publisher => Publisher(
        (publisher \\ "Name").text.trim,
        (publisher \\ "Date_of_Publish").text.trim,
        (publisher \\ "MissingTag").text.trim
      )
    )

    val localTitleList = requirement match {
      case "original" => authorList.zip(publisherList).map(
        authorPublisher => TitleRow(currComment, authorPublisher._1, authorPublisher._2))
      case "new" => authorList.map(author => TitleRow(currComment, author, Publisher(null,null,null))) ++
                    publisherList.map(publisher => TitleRow(currComment, Author(null,null,null), publisher))
    }
    titleList = titleList ++ localTitleList

  }
titleList.toList
/**.map(x => List(x.comment.title,x.comment.description,x.author.author_name,x.author.author_date,x.publisher.publisher_name,x.publisher.publisher_date ))
  */
}

val xml1 = "<books><Comments><Comment><Title>Title1.1</Title><Description>Descript1.1</Description><Authors><Author><Name>Suresh</Name><Date_of_Writing>2017-01-11</Date_of_Writing></Author><Author><Name>Amol</Name><Date_of_Writing>2017-07-17</Date_of_Writing></Author></Authors><Publishers><Publisher><Name>Vishal</Name><Date_of_Publish>2016-01-11</Date_of_Publish></Publisher><Publisher><Name>Arindam</Name><Date_of_Publish>2015-01-11</Date_of_Publish></Publisher></Publishers></Comment></Comments></books>"

val list = parseMyXML(xml1, requirement)
//list.map.foreach(println)


case class TableData(ID_col1:Int,Col2:String,Col3:String,XML_Col4:String)
val data = sc.textFile("file:///home/spark/XML_Project/data.txt")
val rdd = data.map(x=>TableData(x.split(',')(0).toInt, x.split(',')(1), x.split(',')(2), x.split(',')(3)))

val xml_parsed_rdd = rdd.map(x=>(x.ID_col1, x.Col2, x.Col3, parseMyXML(x.XML_Col4, requirement)))
val flattened_rdd = xml_parsed_rdd.flatMap{case(col1,col2,col3,xmlRows) => xmlRows.map(titleRow=>(col1,col2,col3)->titleRow)}
val flattened_list = flattened_rdd.map{case(col1,titleRow) =>(col1._1,col1._2,col1._3,titleRow.comment.title,titleRow.comment.description,titleRow.author.author_name,titleRow.author.author_date,titleRow.publisher.publisher_name,titleRow.publisher.publisher_date)}
//flattened_list.foreach(println)
val df = flattened_list.toDF
df.show
