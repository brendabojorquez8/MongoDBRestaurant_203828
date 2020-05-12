package mongodbtest;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.bson.Document;

public class MongoDBTest {

    public static void main(String[] args) {
        MongoClient mongo = new MongoClient("localhost");
        MongoDatabase db = mongo.getDatabase("test");

        db.listCollectionNames();
        db.createCollection("restaurant");

        MongoCollection<Document> collection = db.getCollection("restaurant");
        
        

        Document d1 = newRestaurant("Carl's Jr", 5, "Fast food,American");
        Document d2 = newRestaurant("La Justa", 4, "Italian,Pizza");
        Document d3 = newRestaurant("Lockers", 5, "American,Pizza,Sports,Bar,Sushi");
        Document d4 = newRestaurant("Oriental Box", 3, "Fast food,Chinese,Japanese,Oriental,Sushi");
        Document d5 = newRestaurant("RRR", 3, "Fast food,Mexican");
        Document d6 = newRestaurant("Sushilito", 3, "Japanese,Sushi");
        
        collection.insertMany(Arrays.asList(d1,d2,d3,d4,d5,d6));
        FindIterable<Document> docs = collection.find();
        System.out.println("Restaurants");
        for (Document doc : docs) {
                System.out.println("name: "+doc.get("name")
                        +"; rating: "+doc.get("rating")
                        +"; cat: "+doc.get("categories"));
        }
        
        System.out.println("\nRating>4");
        docs = collection.find(Filters.gt("rating", 4));
        for (Document doc : docs) {
                System.out.println("name: "+doc.get("name")
                        +"; rating: "+doc.get("rating")
                        +"; cat: "+doc.get("categories"));
        }
        
        System.out.println("\nCategory: Pizza");
        docs = collection.find(
            Filters.regex("categories", Pattern.compile("Pizza")));
        for (Document doc : docs) {
            System.out.println("name: "+doc.get("name")
                        +"; rating: "+doc.get("rating")
                        +"; cat: "+doc.get("categories"));
        }
        
        System.out.println("\nName like 'Sushi'");
        docs=collection.find(
            Filters.regex("name", Pattern.compile("Sushi")));
        for (Document doc : docs) {
            System.out.println("name: "+doc.get("name")
                        +"; rating: "+doc.get("rating")
                        +"; cat: "+doc.get("categories"));
        }
        
                System.out.println("\nAdding category");
        UpdateResult updateResult = collection.updateOne(
                Filters.eq("name", "Sushilito"),
                Updates.set("categories", d6.get("categories")+",Oriental"));
        docs=collection.find(Filters.eq("name", "Sushilito"));
        for (Document doc : docs) {
            System.out.println("name: "+doc.get("name")
                        +"; rating: "+doc.get("rating")
                        +"; cat: "+doc.get("categories"));
        }        
        
        System.out.println("\nDeleting rating<=3");
        DeleteResult deleteResult = collection.deleteMany(Filters.lte("rating", 3));
        docs=collection.find();
        for (Document doc : docs) {
            System.out.println("name: "+doc.get("name")
                        +"; rating: "+doc.get("rating")
                        +"; cat: "+doc.get("categories"));
        } 
        
    }

    public static Document newRestaurant(String name, int rating, String categories) {
        Document d = new Document()
                .append("name", name)
                .append("rating", rating)
                .append("categories", categories);
        return d;
    }

}
