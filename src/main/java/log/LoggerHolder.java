package log;

import org.apache.maven.plugin.logging.Log;

/**
 * @ProjectName: ajunit
 * @Package: log
 * @ClassName: LoggerHolder
 * @Author: 吴成昊
 * @Description: 用于获取log的单例对象
 * @Date: 2019/9/26 13:39
 * @Version: 0.1
 */
public class LoggerHolder {

    public volatile static Log log;


}
