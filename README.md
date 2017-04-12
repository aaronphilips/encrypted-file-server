# encrypted-file-server
uses jni with c encryption

Design note:
I used the the latest encrypt.c and decrypt.c files from the supplied moodle page as is with no modification (you can see these file for yourself). But I did choose how I applied these routine in practice: seeing as how  "TEA operates on two 32-bit unsigned integers"(Wikipedia where I found similar code), but I was tranmitting messages that could be much larger than 2 32-bit ints, I used the TEA encryption routine encode much larger int arrays. So I would run the encryption routine on element 1 and element 2, then on element 2, and element 3 and so on. Given an int array with n ints in them : encrypt(element1,element2),encrypt(element2,element3), encrypt(element3,element4)...encrypt(element(n-1),element(n))
This was accomplished with a simple for loop. This also made mandatory that the smallest message to be encrypted had to be the size of 2 ints. Also, since the encryption ran on ints, if I would want to encrypt messages in other datatypes (mainly bytes and strings), I had to convert the messsage to a byte array, and pad it zero bytes to ensure the byte array was an a size that was a multiple of 4 (or more if i had to pad to ensure the message was at least 2 ints, or 8 bytes long).
Once the bytes are padded appropriatly, they can be encrypted as described above.
Decryption worked the exact opposite way, it ran the decrypt function on the second last byte and last byte, the decrypted the third last byte and second last byte as so:
This essentially used the reverse of above for loop, accounting for the fact that the first value pointer would be the second last byte (n-1) and not (n) and decriment down to the first element
Given an int array with n ints in them : decrypt(element(n-1),element(n)),decrypt(element(n-2),element(n-1))...decrypt(element(1),element(2))
Note the for loops are actually 0 indexed in the code so 0 to (n-1) and (n-2) to 0.
This protocol was not made to send int arrays directly as this could lose data. Current only bytes or strings are supported, but this could be extended.

Some Sources:
http://stackoverflow.com/questions/2165006/simple-java-client-server-program
https://github.com/firatkucuk/diffie-hellman-helloworld/blob/master/src/main/java/com/codvio/examples/diffie_hellman_helloworld/Person.java
http://stackoverflow.com/questions/17003164/byte-array-with-padding-of-null-bytes-at-the-end-how-to-efficiently-copy-to-sma

For idea of using MessageDigest class and md5 hashing, implemented slighlty differently (no point in making salt a string, or hardcoding it anywhere)
http://viralpatel.net/blogs/java-md5-hashing-salting-password/

test files
Mario Paint RickRoll by ihasmario http://www.albinoblacksheep.com/audio/mariopaintroll
tolkien logo in omnivir 'Collection of awesome LOTR posts' http://imgur.com/gallery/DKlrA
