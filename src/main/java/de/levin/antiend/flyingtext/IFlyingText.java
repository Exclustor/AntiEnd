package de.levin.antiend.flyingtext;

import de.levin.antiend.data.Result;
import org.bukkit.Location;

public interface IFlyingText {

    Result create(Location location);
    Result delete();
    Result teleport(Location location);
    void updateDuration(int number);
    void updateStatus(boolean status);
}
