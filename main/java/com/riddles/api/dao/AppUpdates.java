package com.riddles.api.dao;

import javax.persistence.*;

/* Инфа об обновлениях приложения
* В этой таблице всегда одна запись */

@Entity(name = "app_updates")
public class AppUpdates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "current_version")
    private int currentVersion;
    @JoinColumn(name = "required_version")
    private int requiredVersion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(int requiredVersion) {
        this.requiredVersion = requiredVersion;
    }
}
