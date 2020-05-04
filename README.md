# Platform for Standalone Application Development

```
                            Application Daemon
  +----------------------------------------------------------------------+
  |                                                                      |
  |  +------------------------------+  +------------------------------+  |
  |  |                              |  |                              |  |
  |  |       springframework        <--+      Embedded WebServer      |  |
  |  |                              |  |                              |  |
  |  +------------------------------+  +------------------------------+  |
  |                |                                  |                  |
  |  +-------------v----------------+  +--------------v---------------+  |
  |  |                              |  |                              |  |
  |  |      spring-data-jpa         |  |      spring+security         |  |
  |  |                              |  |                              |  |
  |  +------------------------------+  +------------------------------+  |
  |                 |                                 |                  |
  |  +--------------v---------------+  +--------------v---------------+  |
  |  |                              |  |                              |  |
  |  |   JPA(Hibernate)             |  |          Mybatis             |  |
  |  |                              |  |                              |  |
  |  +------------------------------+  +------------------------------+  |
  |                 |                                 |                  |
  |  +--------------v---------------------------------v---------------+  |
  |  |                                                                |  |
  |  |                                                                |  |
  |  |                        Embedded hsqldb                         |  |
  |  +----------------------------------------------------------------+  |
  |                                                                      |
  +----------------------------------------------------------------------+

```

## Demo Site
URL: http://application.oopscraft.net/admin

User/Password : test/test

## Download and Build Binary
First, download the source code from the git hub repository.
And then, executes build shell script. **__(build.sh)__**

```bash
# clones source from github
user@host> git clone https://github.com/oopscraft/application.git

# maven build
user@host> build.sh
```

## Start and Stop Applition
After moving to the installation directory, execute the following command.

```bash
# start
user@host> ./application.sh start

# status
user@host> ./application.sh status

# stop
user@host> ./application.sh stop

# view log
user@host> ./application.sh log
```

## Licence
Anyone can use it freely. Modify the source or allow re-creation. However, you must state that you have the original creator. However, we can not grant patents or licenses for reproductives. (Modifications or reproductions must be shared with the public.)

Licence: LGPL(GNU Lesser General Public License version 3) Copyright (C) 2017 duice.oopscraft.net Contact chomookun@gmail.com

