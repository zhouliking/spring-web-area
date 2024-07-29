package com.joelly.area.file;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class AreaFileReader {

    private static final JsonParser parser = new JsonParser();

    /**
     * 为了缩小一次性内存加载量，进行分省解析返回
     * 文件中的data一定是最后一个出现的key
     */
    public static JsonObject readData(String filePath, String subKey) throws IOException {
        try (JsonReader reader = readFile(filePath)) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (!name.equals("data")) {
                    reader.skipValue();
                    continue;
                }
                if (StringUtils.isEmpty(subKey)) {
                    return parser.parse(reader).getAsJsonObject();
                }
                reader.beginObject();
                while (reader.hasNext()) {
                    String subName = reader.nextName();
                    if (!subName.equals(subKey)) {
                        //parser.parse(reader).getAsJsonObject();
                        continue;
                    }
                    return parser.parse(reader).getAsJsonObject();
                }
            }
        }
        return null;
    }


//    public static void processData(JsonObject jsonData, Area parent, String idPath,
//                                  String namePath, Function<Area, Long> func) {
//        long id = 0;
//        Area thisArea = AreaConvert.fromJson(id, jsonData, parent);
//
//        // add field
//        String mergerId = idPath + "," + thisArea.getId();
//        String mergeName = namePath + "," + thisArea.getName();
//        thisArea.setMergeId(mergerId);
//        thisArea.setMergeName(mergeName);
//        JsonArray array = jsonData.getAsJsonArray("children");
//
//        func.apply(thisArea);
//        if (array != null && array.size() > 0) {
//            array.forEach(c -> {
//                processData(c.getAsJsonObject(), thisArea, mergerId, mergeName, func);
//            });
//        }
//    }



    private static JsonReader readFile(String filePath) throws IOException {
        return new JsonReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath)),
                StandardCharsets.UTF_8));
    }

//    public static void main(String[] args) throws Exception {
//        JsonObject jsonObject = readData("E:/temp/area.data", null);
//        Area parent = new Area();
//        parent.setId(0L);
//        parent.setName("中国");
//
//        AtomicLong atomicLong = new AtomicLong(0);
//
//        processData(jsonObject.getAsJsonObject("zhejiang"), parent, "0", "中国", new Function<Area, Long>() {
//            @Override
//            public Long apply(Area area) {
//                return 0L;
//            }
//        });
//
//        System.out.println(GsonProvider.getInstance().toJson(parent));
//    }
}
