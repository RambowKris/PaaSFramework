package com.example.krb.myawsapplication.mapperClass;

import android.text.Html;
import android.text.Spanned;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by krb on 18/08/2017.
 */

@DynamoDBTable(tableName = "message")
public class MessageClass {
    private String datetime;
    private String identityId;
    private String text;

    //    @DynamoDBIndexRangeKey(attributeName = "name")
    @DynamoDBHashKey(attributeName = "datetime")
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    //    @DynamoDBIndexRangeKey(attributeName = "lastname")
    @DynamoDBAttribute(attributeName = "id_identity")
    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    @DynamoDBAttribute(attributeName = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toHtmlString() {
        return "<b>"+datetime + " " + identityId + ":</b><br>" + text + "<br>";
    }

    public String toString() {
        return datetime + " " + identityId + ": " + text + "\n";
    }

//        @DynamoDBIndexRangeKey(attributeName = "Title")
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }

//        @DynamoDBIndexHashKey(attributeName = "Author")
//        public String getAuthor() {
//            return author;
//        }
//
//        public void setAuthor(String author) {
//            this.author = author;
//        }

//        @DynamoDBAttribute(attributeName = "Price")
//        public int getPrice() {
//            return price;
//        }
//
//        public void setPrice(int price) {
//            this.price = price;
//        }

//        @DynamoDBHashKey(attributeName = "ISBN")
//        public String getIsbn() {
//            return isbn;
//        }

//        public void setIsbn(String isbn) {
//            this.isbn = isbn;
//        }

    //        @DynamoDBAttribute(attributeName = "Hardcover")
//        public Boolean getHardCover() {
//            return hardCover;
//        }
//    public void setHardCover(Boolean hardCover) {
//        this.hardCover = hardCover;
//    }
}
