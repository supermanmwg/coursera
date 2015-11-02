package com.utils.rjos;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CourseRjo extends BaseRjo {

    private Linked linked;
    private List<Element> elements;

    public Linked getLinked() {
        return linked;
    }

    public List<Element> getElements() {
        return elements;
    }

    public static class Element {
        private int id;
        private String name;
        private String shortName;
        private String largeIcon;
        private String estimatedClassWorkload;
        private Link links;

        public Element() {
        }

        public Element(int id, String name, String shortName, String largeIcon, String estimatedClassWorkload, Link links) {
            this.id = id;
            this.name = name;
            this.shortName = shortName;
            this.largeIcon = largeIcon;
            this.estimatedClassWorkload = estimatedClassWorkload;
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

        public String getLargeIcon() {
            return largeIcon;
        }

        public String getEstimatedClassWorkload() {
            return estimatedClassWorkload;
        }

        public Link getLinks() {
            return links;
        }
    }

    public static class Linked {
        private List<Universities> universities;

        public Linked() {
        }

        public Linked(List<Universities> universities) {
            this.universities = universities;
        }

        public List<Universities> getUniversities() {
            return universities;
        }
    }

    public static class Universities {
        private int id;
        private String shortName;

        public Universities() {
        }

        public Universities(int id, String shortName) {
            this.id = id;
            this.shortName = shortName;
        }

        public int getId() {
            return id;
        }

        public String getShortName() {
            return shortName;
        }
    }

    public static class Link {
        private List<Integer> universities;

        public Link() {
        }

        public Link(List<Integer> universities) {
            this.universities = universities;
        }

        public List<Integer> getUniversities() {
            return universities;
        }
    }

    public String getUniversities(Link link) {
        String names ="";
        for(int i = 0; i < link.getUniversities().size(); i++) {
            int id = link.getUniversities().get(i);
            for(int j = 0; j < linked.getUniversities().size(); j++) {
                if(linked.getUniversities().get(j).getId() == id) {
                    names += linked.getUniversities().get(j).getShortName() + " university\n";
                }
            }
        }

        return names;
    }
}
