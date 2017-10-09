package fi.jamk.l3329.mapsjsonexample;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Peter on 7.10.2017.
 */

public class Course {
    private String type;
    private LatLng latLng;
    //    private float lat;
//    private float lng;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String web;
    private String image;
    private String text;
    private String course;

    private Course(CourseBuilder cb) {
        this.type = cb.type;
//        this.lat = cb.lat;
//        this.lng = cb.lng;
        this.name = cb.name;
        this.address = cb.address;
        this.phone = cb.phone;
        this.email = cb.email;
        this.web = cb.web;
        this.image = cb.image;
        this.text = cb.text;
        this.course = cb.course;
        this.latLng = cb.latLng;
    }

    public static class CourseBuilder {
        private String type;
        private LatLng latLng;
        //        private float lat;
//        private float lng;
        private String name;
        private String address;
        private String phone;
        private String email;
        private String web;
        private String image;
        private String text;
        private String course;


        public CourseBuilder(String type) {
            this.type = type;
        }

        public CourseBuilder() {

        }

        public Course build(){
            return new Course(this);
        }

        public CourseBuilder latLng(LatLng latLng){
            this.latLng = latLng;
            return this;
        }

        public CourseBuilder course(String course){
            this.course = course;
            return this;
        }

        public CourseBuilder type(String type) {
            this.type = type;
            return this;
        }

//        public CourseBuilder lat(float lat) {
//            this.lat = lat;
//            return this;
//        }
//
//        public CourseBuilder lng(float lng) {
//            this.lng = lng;
//            return this;
//        }

        public CourseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder address(String address) {
            this.address = address;
            return this;
        }

        public CourseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public CourseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CourseBuilder web(String web) {
            this.web = web;
            return this;
        }

        public CourseBuilder image(String image) {
            this.image = image;
            return this;
        }

        public CourseBuilder text(String text) {
            this.text = text;
            return this;
        }
    }

    public String getType() {
        return type;
    }

//    public float getLat() {
//        return lat;
//    }
//
//    public float getLng() {
//        return lng;
//    }


    public LatLng getLatLng() {
        return latLng;
    }

    public String getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWeb() {
        return web;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
