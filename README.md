# NotCore (NC)

## Including into a Java Project

### Step 1 ***Add dependency***

~~~xml
        <dependency>
            <groupId>com.bbva.intranet</groupId>
            <artifactId>not-core</artifactId>
            <version>1.0.0</version>
        </dependency>
~~~

### Step 2 ***Add `GNotifier`(GN) data configuration into properties files***

>`ns.appKey` value can be `artifactId`;

>`ns.sender.email` must be String value and email of Scrum Master «user@bbva.com». To development enviroment ends @dev.bbva.com;

> `ns.id.application` must be Long value. This fie is provided by GNotifier;

> `ns.sender.id` must be String value. This fie is provided by GNotifier «e.g. bbva-project-name»;

> `ns.name` must be String value. This fie is provided by MSA document;

> `ns.version` must be String value. This fie`ns.version` must be String value. This field is created by development team;ld is created by development team;

> `ns.generic.template` must be Long value. This field is create from GNotifier, an email as example below to create a generic template.

~~~json
{
   "name": "nameProjectGeneric",
   "properties": [
       {
           "regExp": "(.+|\n)+",
           "name": "title"
       },
       {
           "regExp": "(.+|\n)+",
           "name": "body"
       }
   ],
   "defaultLanguage": "es_ES",
   "languages": {
       "es_ES": {
           "notification": {
               "title": "<<title>>",
               "body": "<<body>>"
           }
       }
   }
}
~~~

### Step 3 ***Add spring configuration to use module***

~~~xml
            <context:component-scan base-package="com.bbva.intranet.not.core"/>
            <context:component-scan base-package="com.bbva.intranet.storage"/>
            <context:component-scan base-package="com.bbva.intranet.senders"/>
            
            <bean id="gnSender" class="com.bbva.intranet.senders.domain.dao.impl.GNotifierImpl"/>
            <bean id="fcmSender" class="com.bbva.intranet.senders.domain.dao.impl.FCMImpl"/>
            
            <bean id="notCore" class="com.bbva.intranet.not.core.dao.impl.NotCoreImpl">
                <constructor-arg name="gnSender" ref="gnSender"/>
                <constructor-arg name="fcmSender" ref="fcmSender"/>
            </bean>
            
            <bean id="notCoreDeviceDAO" class="com.bbva.intranet.storage.domain.dao.impl.NotCoreDeviceDAOImpl">
                <constructor-arg name="sessionFactory" ref="sessionFactory"/>
            </bean>
            <bean id="notCoreNotificationDAO" class="com.bbva.intranet.storage.domain.dao.impl.NotCoreNotificationDAOImpl">
                <constructor-arg name="sessionFactory" ref="sessionFactory"/>
            </bean>
            <bean id="notCoreNotificationSentDAO" class="com.bbva.intranet.storage.domain.dao.impl.NotCoreNotificationSentDAOImpl">
                <constructor-arg name="sessionFactory" ref="sessionFactory"/>
            </bean>
            <bean id="notCoreTopicDAO" class="com.bbva.intranet.storage.domain.dao.impl.NotCoreTopicDAOImpl">
                <constructor-arg name="sessionFactory" ref="sessionFactory"/>
            </bean>
            <bean id="notCoreUserDAO" class="com.bbva.intranet.storage.domain.dao.impl.NotCoreUserDAOImpl">
                <constructor-arg name="sessionFactory" ref="sessionFactory"/>
            </bean>
            <bean id="notCoreUserTopicDAO" class="com.bbva.intranet.storage.domain.dao.impl.NotCoreNotCoreUserTopicDAOImpl">
                <constructor-arg name="sessionFactory" ref="sessionFactory"/>
            </bean>
            
            
            <bean id="notCoreStorage" class="com.bbva.intranet.not.core.dao.impl.NotCoreStorageImpl">
                <constructor-arg index="0" ref="gnSender"/>
                <constructor-arg index="1" ref="fcmSender"/>
                <constructor-arg index="2" ref="notCoreDeviceDAO"/>
                <constructor-arg index="3" ref="notCoreNotificationDAO"/>
                <constructor-arg index="4" ref="notCoreNotificationSentDAO"/>
                <constructor-arg index="5" ref="notCoreTopicDAO"/>
                <constructor-arg index="6" ref="notCoreUserDAO"/>
                <constructor-arg index="7" ref="notCoreUserTopicDAO"/>
            </bean>
~~~



### Using NC

#### NotCoreUtility class

NotCoreUtility have many resources to interact with GN. Every you need as parameter to call any method, you can find into this class.

*   Examples:
    ~~~java
    import com.bbva.intranet.not.core.utilities.NotCoreUtility;
    class Example {
        void doSomething() {       
            NotCoreUtility.buildApplicationPN(); // Return a ApplicationPN object to send a push notification.
            NotCoreUtility.NC_APPLICATION_ID;   // Return a Long value. Already configured
        }
    }
    ~~~

#### NotCoreChannel enum

NotCoreChannel have two options to chose. Depends how you want send the notification GN or FCM.

#### Use simple NotCore version
*   This version provide full functionality to use GNotifier. You can use their classes and methods like this example:
    
~~~java
import com.bbva.intranet.senders.domain.requests.desregister.Desregister;
import com.bbva.intranet.not.core.dao.NotCore;
import com.bbva.intranet.not.core.exceptions.NotCoreException;
import com.bbva.intranet.not.core.utilities.NotCoreChannel;
import com.bbva.intranet.not.core.utilities.NotCoreUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Example {
    
    private static final Logger LOG = LoggerFactory.getLogger(Example.class);
    
    @Autowired
    private NotCore notCore;
    
    public Example() {
        
    }
    
    void doSomething() {
            try {
                String email = "user.name@bbva.com";
                String uniqueDeviceId = "1KS83HS3DIS";
                Desregister desregister = NotCoreUtility.buildDesregister(email, uniqueDeviceId);
                notCore.desRegister(NotCoreChannel.GNOTIFIER, desregister);
            } catch (NotCoreException e) {
                LOG.error(e.getMessage());
                throw new AVAException("GNotifier no pudo remover registro");
            }
    }
}
~~~

#### Use `NotCoreStorageImpl` version

This version provide the same functionality than first and 