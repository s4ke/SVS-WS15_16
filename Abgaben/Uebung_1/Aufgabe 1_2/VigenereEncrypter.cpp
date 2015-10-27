//Martin Braun (1249080), Julian Neuberger (1252734)
#include "VignereEncrypter.h"

VigenereEncrypter::VigenereEncrypter(std::string* key) : key(*key) {
	this->keyLength = key->length();
}

void VigenereEncrypter::encrypt(std::string* in, std::string* encrypted) {
	this->helper(in, encrypted, true);
}

void VigenereEncrypter::decrypt(std::string* in, std::string* decrypted) {
	this->helper(in, decrypted, false);
}

void VigenereEncrypter::helper(std::string* in, std::string* out, bool encrypt) {
	int index = 0;
	int keyIndex;
	int keyChar;
	char encodedChar;
	int curValue;
	int encodedValue;
	for(std::string::iterator iter = in->begin(); iter != in->end(); ++iter) {
		keyIndex = index % (this->keyLength);
		keyChar = ((int) this->key.at(keyIndex)) - VigenereEncrypter::ALPHABET_OFFSET;
		if(*iter == '_') {
			curValue = VigenereEncrypter::SPACE_VALUE;
		} else {
			curValue = (int) *iter;
		}
		curValue -= VigenereEncrypter::ALPHABET_OFFSET;

		int tmp;
		if(encrypt) {
			tmp = curValue + keyChar;
		} else {
			tmp = curValue - keyChar;
		}
		tmp = tmp % VigenereEncrypter::ALPHABET_SIZE;
		if(tmp < 0) {
			tmp = VigenereEncrypter::ALPHABET_SIZE + tmp;
		}
		//std::cout << tmp << "|";

		encodedValue = tmp + VigenereEncrypter::ALPHABET_OFFSET;
		
		if(encodedValue == VigenereEncrypter::SPACE_VALUE) {
			encodedChar = '_';
		} else {
			encodedChar = (char) encodedValue;
		}

		*out += encodedChar;
		++index;
	}
}