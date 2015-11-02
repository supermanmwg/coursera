package com.utils.rjos;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-2.
 */
public class CourseDetailRjo extends BaseRjo{
    private List<Element> elements;

    public CourseDetailRjo() {
    }

    public CourseDetailRjo(List<Element> elements) {
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public static class Element {
        private int id;
        private String name;
        private String language;
        private String photo;
        private String shortDescription;
        private String aboutTheCourse;
        private String instructor;
        private String estimatedClassWorkload;

        public Element() {
        }

        public Element(int id, String name, String language, String photo,
                       String shortDescription, String aboutTheCourse,
                       String instructor, String estimatedClassWorkload) {
            this.id = id;
            this.name = name;
            this.language = language;
            this.photo = photo;
            this.shortDescription = shortDescription;
            this.aboutTheCourse = aboutTheCourse;
            this.instructor = instructor;
            this.estimatedClassWorkload = estimatedClassWorkload;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLanguage() {
            return language;
        }

        public String getPhoto() {
            return photo;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getAboutTheCourse() {
            return aboutTheCourse;
        }

        public String getInstructor() {
            return instructor;
        }

        public String getEstimatedClassWorkload() {
            return estimatedClassWorkload;
        }
    }
}
