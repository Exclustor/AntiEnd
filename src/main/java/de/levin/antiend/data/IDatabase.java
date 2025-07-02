package de.levin.antiend.data;

import de.levin.antiend.viewmodel.FlyingTextViewModel;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public interface IDatabase {

    /**
     * UUID    -> Bukkit Entity <br>
     * Boolean -> Is updatable
     * */
    Result save(HashSet<FlyingTextViewModel> flyingTextViewModels);
    Result delete();
    boolean getEndStatus();
    void setEndStatus(boolean endStatus);
    boolean exists();
    HashSet<FlyingTextViewModel> getAll();
    int getDurationEndDisabled();
    void setDurationEndDisabled(int duration);
}

