package io.kimmking.javacourse.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/22 16:47
 */
@Data
@AllArgsConstructor
public class ConsConfigs {

    private List<ConsConfig> config;

}
