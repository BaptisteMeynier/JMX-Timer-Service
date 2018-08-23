package com.java.monitoring.jmx.agent;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.util.Date;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;


/**
 * rmiregistry 9000
 * Le service Timer permet d'envoyer des notifications predefinies a un moment donne ou periodiquement. 
 * Ces notifications sont envoyees à tous les objets qui se sont abonnés pour recevoir les notifications
 * emises par le service. Les caracteristiques de l emission d une notification sont assez souples : elle 
 * demarre a une certaine date/heure et est repetee a intervalles de temps reguliers durant une periode 
 * ou pour un certain nombre d occurrences. Le service Timer est implemente sous la forme d'un MBean ce 
 * qui permet de l administrer au travers de JMX lui-meme. Les notifications emises par le service Timer 
 * sont de type TimerNotification.L implementation du service Timer est encapsulee dans la classe 
 * javax.management.timer.Timer
 * 
 * @author baptiste
 *
 */
public class LancerAgentAvecTimer {

	public static void main(String[] args) {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		try {

			// instanciation et enregistrement du service Timer
			javax.management.timer.Timer timer = new javax.management.timer.Timer();
			mbs.registerMBean(timer, new ObjectName("Services:type=Timer"));

			// ajout de la definition des notifications
			timer.addNotification(
					"Register", 
					"Test du service timer", 
					new String(),
					new Date(), 
					5000, 
					0);
			timer.setSendPastNotifications(false);
			timer.start();

			// Creation et demarrage du connecteur RMI
			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://localhost:9000/server");
			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(
					url, null, mbs);
			cs.start();
			System.out.println("Lancement connecteur RMI " + url);

			while (true) {
				Thread.sleep(1000);
			}

		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
