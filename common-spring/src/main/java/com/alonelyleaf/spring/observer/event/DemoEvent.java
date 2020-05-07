package com.alonelyleaf.spring.observer.event;

/**
 * @author bijl
 * @date 2020/5/6
 */
public interface DemoEvent {

    class Common {

        private String id;

        public String getId() {
            return id;
        }

        public Common setId(String id) {
            this.id = id;
            return this;
        }
    }

    class AddEdit extends Common{

        private String name;

        public String getName() {
            return name;
        }

        public AddEdit setName(String name) {
            this.name = name;
            return this;
        }
    }

    class Delete extends Common{

    }
}
