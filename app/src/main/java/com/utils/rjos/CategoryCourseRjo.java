package com.utils.rjos;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryCourseRjo extends BaseRjo {
    private List<Element> elements;

    public CategoryCourseRjo() {
    }

    public CategoryCourseRjo(List<Element> elements) {
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public static class Element {
        private int id;
        private String name;
        private String shortName;
        private Link links;

        public Element() {
        }

        public Element(int id, String name, String shortName, Link links) {
            this.id = id;
            this.name = name;
            this.shortName = shortName;
            this.links = links;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        public Link getLinks() {
            return links;
        }
    }

    public static class Link {

        private List<Integer> courses;

        public Link() {
        }

        public Link(List<Integer> courses) {
            this.courses = courses;
        }

        public List<Integer> getCourses() {
            return courses;
        }
    }
}
