package com.cib.edip.edipsftpserver.sftpd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

public class PublicKeyHelper {
    public static final Charset US_ASCII = Charset.forName("US-ASCII");

    public static String getEncodedPublicKey(final PublicKey pub) {
        if (pub instanceof RSAPublicKey) {
            return encodeRSAPublicKey((RSAPublicKey) pub);
        }
        if (pub instanceof DSAPublicKey) {
            return encodeDSAPublicKey((DSAPublicKey) pub);
        }
        return null;
    }

    public static String encodeRSAPublicKey(final RSAPublicKey key) {
        final BigInteger[] params = new BigInteger[] {
                key.getPublicExponent(), key.getModulus()
        };
        return encodePublicKey(params, "ssh-rsa");
    }

    public static String encodeDSAPublicKey(final DSAPublicKey key) {
        final BigInteger[] params = new BigInteger[] {
                key.getParams().getP(), key.getParams().getQ(), key.getParams().getG(), key.getY()
        };
        return encodePublicKey(params, "ssh-dss");
    }

    private static final void encodeUInt32(final ByteBuf bab, final int value) {
        bab.writeByte((byte) ((value >> 24) & 0xFF));
        bab.writeByte((byte) ((value >> 16) & 0xFF));
        bab.writeByte((byte) ((value >> 8) & 0xFF));
        bab.writeByte((byte) (value & 0xFF));
    }

    private static String encodePublicKey(final BigInteger[] params, final String keyType) {
        final ByteBuf bab = ByteBufAllocator.DEFAULT.ioBuffer(256);
        //bab.setAutoExpand(true);
        byte[] buf = null;
        // encode the header "ssh-dss" / "ssh-rsa"
        buf = keyType.getBytes(US_ASCII); // RFC-4253, pag.13
        encodeUInt32(bab, buf.length);    // RFC-4251, pag.8 (string encoding)
        for (final byte b : buf) {
            bab.writeByte(b);
        }
        // encode params
        for (final BigInteger param : params) {
            buf = param.toByteArray();
            encodeUInt32(bab, buf.length);
            for (final byte b : buf) {
                bab.writeByte(b);
            }
        }
        //bab.flip();
        //buf = new byte[bab.limit()];

        //System.arraycopy(bab.array(), 0, buf, 0, buf.length);
        //bab.free();
        if(bab.hasArray()){
            buf=bab.array();
        }else{
            int length = bab.readableBytes();//2
            buf = new byte[length];    //3
            bab.getBytes(bab.readerIndex(), buf);
        }
        bab.clear();

        String rs=keyType + " " + DatatypeConverter.printBase64Binary(buf);
        return rs;
    }

}
