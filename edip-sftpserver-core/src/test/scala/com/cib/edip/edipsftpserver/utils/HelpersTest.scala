package com.cib.edip.edipsftpserver.utils;

import org.testng.annotations.Test;

import java.util.HashMap;

import org.testng.Assert.{_};

class HelpersTest {

    @Test
    def testGenUUID(): Unit={

    }

    @Test
    def testCastMapToJSON(): Unit= {
    }

    @Test
   def testCastJSONToMap():Unit= {
    }

    @Test
    def testCheckNotNullAndEmpty():Unit= {

        Helpers.checkNotNullAndEmpty(new HashMap[Any,Any]())

    }

    @Test
    def testCheckNotNull():Unit= {
    }
}