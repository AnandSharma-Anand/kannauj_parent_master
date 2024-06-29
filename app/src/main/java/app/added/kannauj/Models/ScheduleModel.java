package app.added.kannauj.Models;

import java.util.List;

public class ScheduleModel {

    String id,name;
    List<TopicModel> topicList;

    public ScheduleModel(String id, String name, List<TopicModel> topicList) {
        this.id = id;
        this.name = name;
        this.topicList = topicList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TopicModel> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopicModel> topicList) {
        this.topicList = topicList;
    }
}
