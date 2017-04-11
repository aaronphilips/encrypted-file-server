#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <jni.h>
#include "TEA.h"
// #include "encrypt.c"
// #include "decrypt.c"
void decrypt (int *v, int *k);
void encrypt (int *v, int *k);

JNIEXPORT void JNICALL Java_TEA_nativeEncrypt
(JNIEnv *env,jobject callingObject, jintArray valueJava, jintArray keyJava) {
  // printf("encrypt started\n");
  jsize value_length = (*env)->GetArrayLength(env, valueJava);
  jint *value_elements = (*env)->GetIntArrayElements(env, valueJava, 0);

  jsize key_length = (*env)->GetArrayLength(env, keyJava);
  jint *key_elements = (*env)->GetIntArrayElements(env, keyJava, 0);

  // encrypts everything in groups of two bytes, starting at the first 2
  // printf("%d\n",value_length);
  int i;
  // for(i=0;i<value_length;i++){
  //   printf("%d\n",value_elements[i]);
  // }
  for(i=0;i<value_length-1;i++){
    encrypt(&value_elements[i],key_elements);
  }
  // for(i=0;i<value_length;i++){
  //   printf("%d\n",value_elements[i]);
  // }
  (*env)->ReleaseIntArrayElements(env, valueJava, value_elements, 0);
  (*env)->ReleaseIntArrayElements(env, keyJava, key_elements, 0);

  // printf("encrypt ended\n");
  return;
}

JNIEXPORT void JNICALL Java_TEA_nativeDecrypt
(JNIEnv *env,jobject callingObject, jintArray valueJava, jintArray keyJava) {
  // printf("decrypt stared\n");
  jsize value_length = (*env)->GetArrayLength(env, valueJava);
  jint *value_elements = (*env)->GetIntArrayElements(env, valueJava, 0);

  jsize key_length = (*env)->GetArrayLength(env, keyJava);
  jint *key_elements = (*env)->GetIntArrayElements(env, keyJava, 0);

  // decrypts everything in groups of two bytes, starting at the last 2
  int i;
  for(i=value_length-2;i>=0;i--){
    decrypt(&value_elements[i],key_elements);
  }

  (*env)->ReleaseIntArrayElements(env, valueJava, value_elements, 0);
  (*env)->ReleaseIntArrayElements(env, keyJava, key_elements, 0);
  // printf("decrypt ended\n");
  return;
}
//
// void decrypt (int *v, int *k){
// /* TEA decryption routine */
// unsigned int n=32, sum, y=v[0], z=v[1];
// unsigned int delta=0x9e3779b9l;
//
// 	sum = delta<<5;
// 	while (n-- > 0){
// 		z -= (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
// 		y -= (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
// 		sum -= delta;
// 	}
// 	v[0] = y;
// 	v[1] = z;
// }
//
// void encrypt (int *v, int *k){
// /* TEA encryption algorithm */
// unsigned int y = v[0], z=v[1], sum = 0;
// unsigned int delta = 0x9e3779b9, n=32;
//
// 	while (n-- > 0){
// 		sum += delta;
// 		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
// 		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
// 	}
//
// 	v[0] = y;
// 	v[1] = z;
// }
