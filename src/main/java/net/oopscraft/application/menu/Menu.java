package net.oopscraft.application.menu;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_MENU_INFO")
@EntityListeners(SystemEntityListener.class)
public class Menu extends SystemEntity {

	
	
}
