package com.utils.rjos;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryRjo extends BaseRjo {

    private List<Element> elements;

    public CategoryRjo() {
    }

    public CategoryRjo(List<Element> elements) {
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public static class Element {
        private int id;
        private String name;
        private String shortName;
        public Element() {
        }

        public Element(int id, String name, String shortName) {
            this.id = id;
            this.name = name;
            this.shortName = shortName;
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
    }
}
