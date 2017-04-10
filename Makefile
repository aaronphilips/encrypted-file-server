all: clean ServerRunnable.class Server.class Client.class EncryptedCommunicator.class TEA.class TEA.h lib_TEA.so
	@echo Load path: "${LD_LIBRARY_PATH}"
		@echo 'You must ensure loadLibrary is set correctly (contains current directory)'
		@echo 'You can do this by running this command once: "export LD_LIBRARY_PATH=$$LD_LIBRARY_PATH:."'

ServerRunnable.class:ServerRunnable.java
	javac ServerRunnable.java

Server.class:
	javac Server.java
Client.class:
	javac Client.java

EncryptedCommunicator.class:EncryptedCommunicator.java
	javac EncryptedCommunicator.java

TEA.class:TEA.java
	javac TEA.java

TEA.h:TEA.class
	javah -jni TEA

lib_TEA.so:lib_TEA.c
	gcc -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -shared -fpic -o lib_TEA.so lib_TEA.c


clean:
	rm -f *.class
	rm -f *.so
	rm -f *.h

# all: DataGenerator.class DataSorter.class InsertionSort.class InsertionSort.h lib_insertionsort.so
# 	@echo Load path: "${LD_LIBRARY_PATH}"
# 	@echo 'You must ensure loadLibrary is set correctly (contains current directory)'
# 	@echo 'You can do this by running this command once: "export LD_LIBRARY_PATH=$$LD_LIBRARY_PATH:."'
#
# DataGenerator.class:DataGenerator.java
# 	javac DataGenerator.java
#
# DataSorter.class:DataSorter.java
# 	javac DataSorter.java
#
# InsertionSort.class:InsertionSort.java
# 	javac InsertionSort.java
#
# InsertionSort.h:InsertionSort.class
# 	javah -jni InsertionSort
#
# lib_insertionsort.so:lib_insertionsort.c
# 	gcc -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -shared -fpic -o lib_insertionsort.so lib_insertionsort.c
#
#
# clean:
# 	rm -f *.class
# 	rm -f *.so
# 	rm -f *.h
