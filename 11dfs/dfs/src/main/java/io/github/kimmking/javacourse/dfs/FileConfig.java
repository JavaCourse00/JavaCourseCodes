package io.github.kimmking.javacourse.dfs;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;
import java.io.FileNotFoundException;

@Configuration
public class FileConfig extends WebMvcConfigurationSupport {

    public static final String PATH = new File("app").getAbsolutePath();
    public static final String PIC_PATH=PATH + "/pic/";
    static {
        File picFile = new File(PIC_PATH);
        if (!picFile.exists()) {
            picFile.mkdirs();
        }
        System.out.println("PIC_PATH => " + PIC_PATH);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        File path = null;
//        try {
//            path = new File(ResourceUtils.getURL("classpath:").getPath());
//            System.out.println("ResourceUtils Path: "+ path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String picPath = path.getParentFile().getParentFile().getParent() + File.separator + "app" + File.separator + "pic" + File.separator;
        String picPath = PIC_PATH;
        System.out.println("addResource for: "+picPath);
        registry.addResourceHandler("/pic/**").addResourceLocations("file:"+picPath);
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
        super.addResourceHandlers(registry);
    }
}
