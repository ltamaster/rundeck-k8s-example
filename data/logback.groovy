import org.rundeck.util.logback.TrueConsoleAppender
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter
appender('STDOUT', TrueConsoleAppender){
encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

['rundeckpro.services.HeartbeatService'].each {
    logger it, DEBUG, ['STDOUT'], false
}

['rundeckpro.services.TakeoverService'].each {
    logger it, DEBUG, ['STDOUT'], false
}

['rundeck.services.ScheduledExecutionService'].each {
    logger it, DEBUG, ['STDOUT'], false
}

['rundeck.quartzjobs.ExecutionsCleanUp'].each {
    logger it, DEBUG, ['STDOUT'], false
}

['rundeckpro.services.ExecutionDelegationService'].each {
    logger it, DEBUG, ['STDOUT'], false
}

