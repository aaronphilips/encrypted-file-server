#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <jni.h>
#include "TEA.h"


JNIEXPORT void JNICALL Java_TEA_encrypt
(JNIEnv *env,jobject callingObject, jintArray valueJava, jintArray outputFileNameJava) {
  printf("encrypt\n");
}

JNIEXPORT void JNICALL Java_TEA_decrypt
(JNIEnv *env,jobject callingObject, jintArray valueJava, jintArray outputFileNameJava) {
  printf("decrypt\n");
}
