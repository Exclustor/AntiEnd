package de.levin.antiend.viewmodel;

import lombok.Data;

import java.util.UUID;

@Data
public class FlyingTextViewModel {

    private UUID uuid;
    private String originalText;

    public FlyingTextViewModel(UUID uuid, String originalText){
        this.uuid = uuid;
        this.originalText = originalText;
    }
}
