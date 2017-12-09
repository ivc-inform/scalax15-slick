package queries

import slick.jdbc.H2Profile.api._

import scala.concurrent._
import scala.concurrent.duration._

object Main {

    // Tables -------------------------------------

    case class Album(
                      artist: String,
                      title: String,
                      year: Int,
                      rating: Rating,
                      id: Long = 0L)

    class AlbumTable(tag: Tag) extends Table[Album](tag, "albums") {
        def artist = column[String]("artist")
        def title = column[String]("title")
        def year = column[Int]("year")
        def rating = column[Rating]("rating")
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

        def * = (artist, title, year, rating, id) <> (Album.tupled, Album.unapply)
    }

    lazy val albumQuery = TableQuery[AlbumTable]


    // Example queries ----------------------------

    val createTableAction = albumQuery.schema.create

    val insertAlbumsAction =
        albumQuery ++= Seq(
            Album("Keyboard Cat", "Keyboard Cat's Greatest Hits", 2009, Rating.Awesome),
            Album("Spice Girls", "Spice", 1996, Rating.Good),
            Album("Rick Astley", "Whenever You Need Somebody", 1987, Rating.NotBad),
            Album("Manowar", "The Triumph of Steel", 1992, Rating.Meh),
            Album("Justin Bieber", "Believe", 2013, Rating.Aaargh))

    val selectAllQuery = albumQuery

    val selectWhereQuery = albumQuery.filter(_.rating === (Rating.Awesome: Rating))

    val selectSortedQuery1 = albumQuery.sortBy(_.year.asc)

    val selectSortedQuery2 =
        albumQuery
          .sortBy(a => (a.year.asc, a.rating.asc))

    val selectPagedQuery =
        albumQuery
          .drop(2).take(1)

    val selectColumnsQuery1 =
        albumQuery
          .map(_.title)

    val selectColumnsQuery2 =
        albumQuery
          .map(a => (a.artist, a.title))

    val selectCombinedQuery =
        albumQuery
          .filter(_.artist === "Keyboard Cat")
          .map(_.title)


    // Returning single/multiple results ----------

    val selectPagedAction1 =
        selectPagedQuery
          .result

    val selectPagedAction2 =
        selectPagedQuery
          .result
          .headOption

    val selectPagedAction3 =
        selectPagedQuery
          .result
          .head


    // Database -----------------------------------

    val db = Database.forConfig("scalaxdb")


    // Let's go! ----------------------------------

    def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 2 seconds)

    def createTestAlbums() = {
        exec(createTableAction)
        exec(insertAlbumsAction)
    }

    def main(args: Array[String]): Unit = {
        createTestAlbums()
        exec(selectCombinedQuery.result).foreach(println)
    }

}
