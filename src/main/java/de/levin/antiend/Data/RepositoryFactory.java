package de.levin.antiend.Data;

import de.levin.antiend.AntiEnd;
import de.levin.antiend.other.Translation;
import lombok.var;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class RepositoryFactory {

    public static <T> void save(T data, String fileName) {
        try (var writer = new FileWriter(fileName)) {
            new Yaml().dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }
    }

    public static <T> T load(Class<T> clazz, String filePath) {
        var constructor = new Constructor(clazz, new LoaderOptions());
        try (var inputStream = new FileInputStream(AntiEnd.getInstance().getDataFolder()
                + File.separator + filePath)) {
            return new Yaml(constructor).loadAs(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + filePath, e);
        }
    }
}
