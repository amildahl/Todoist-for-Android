/*    
	This file is part of Todoist for Android™.

    Todoist for Android™ is free software: you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published 
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Todoist for Android™ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Todoist for Android™.  If not, see <http://www.gnu.org/licenses/>.
    
    This file incorporates work covered by the following copyright and  
 	permission notice:
 	
 	Copyright [2010] pskink <pskink@gmail.com>
 	Copyright [2010] ys1382 <ys1382@gmail.com>
 	Copyright [2010] JonTheNiceGuy <JonTheNiceGuy@gmail.com>

   	Licensed under the Apache License, Version 2.0 (the "License");
   	you may not use this file except in compliance with the License.
   	You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   	Unless required by applicable law or agreed to in writing, software
   	distributed under the License is distributed on an "AS IS" BASIS,
   	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   	See the License for the specific language governing permissions and
   	limitations under the License.
*/

package com.android.applications.todoist.handlers;

public final class WebRequest{
    // Saved response.
    private java.util.Map<String,java.util.List<String>> responseHeader = null;
    private java.net.URL responseURL = null;
    private int responseCode = -1;
    private String MIMEtype  = null;
    private String charset   = null;
    private Object content   = null;
 
    /** Open a web file. */
    public WebRequest( String urlString )
        throws java.net.MalformedURLException, java.io.IOException {
        // Open a URL connection.
        final java.net.URL url = new java.net.URL( urlString );
        final java.net.URLConnection uconn = url.openConnection( );
        if ( !(uconn instanceof java.net.HttpURLConnection) )
            throw new java.lang.IllegalArgumentException(
                "URL protocol must be HTTP." );
        final java.net.HttpURLConnection conn =
            (java.net.HttpURLConnection)uconn;
 
        // Set up a request.
        conn.setConnectTimeout( 10000 );    // 10 seconds
        conn.setReadTimeout( 10000 );       // 10 seconds
        conn.setInstanceFollowRedirects( true );
        conn.setRequestProperty( "User-agent", "spider" );
 
        // Send the request.
        conn.connect( );
 
        // Get the response.
        responseHeader    = conn.getHeaderFields( );
        responseCode      = conn.getResponseCode( );
        responseURL       = conn.getURL( );
        final int length  = conn.getContentLength( );
        final String type = conn.getContentType( );
        if ( type != null ) {
            final String[] parts = type.split( ";" );
            MIMEtype = parts[0].trim( );
            for ( int i = 1; i < parts.length && charset == null; i++ ) {
                final String t  = parts[i].trim( );
                final int index = t.toLowerCase( ).indexOf( "charset=" );
                if ( index != -1 )
                    charset = t.substring( index+8 );
            }
        }
 
        // Get the content.
        final java.io.InputStream stream = conn.getErrorStream( );
        if ( stream != null )
            content = readStream( length, stream );
        else if ( (content = conn.getContent( )) != null &&
            content instanceof java.io.InputStream )
            content = readStream( length, (java.io.InputStream)content );
        conn.disconnect( );
    }
 
    /** Read stream bytes and transcode. */
    private Object readStream( int length, java.io.InputStream stream )
        throws java.io.IOException {
        final int buflen = Math.max( 1024, Math.max( length, stream.available() ) );
        byte[] buf   = new byte[buflen];;
        byte[] bytes = null;
 
        for ( int nRead = stream.read(buf); nRead != -1; nRead = stream.read(buf) ) {
            if ( bytes == null ) {
                bytes = buf;
                buf   = new byte[buflen];
                continue;
            }
            final byte[] newBytes = new byte[ bytes.length + nRead ];
            System.arraycopy( bytes, 0, newBytes, 0, bytes.length );
            System.arraycopy( buf, 0, newBytes, bytes.length, nRead );
            bytes = newBytes;
        }
 
        if ( charset == null )
            return bytes;
        try {
            return new String( bytes, charset );
        }
        catch ( java.io.UnsupportedEncodingException e ) { }
        return bytes;
    }
 
    /** Get the content. */
    public Object getContent( ) {
        return content;
    }
 
    /** Get the response code. */
    public int getResponseCode( ) {
        return responseCode;
    }
 
    /** Get the response header. */
    public java.util.Map<String,java.util.List<String>> getHeaderFields( ) {
        return responseHeader;
    }
 
    /** Get the URL of the received page. */
    public java.net.URL getURL( ) {
        return responseURL;
    }
 
    /** Get the MIME type. */
    public String getMIMEType( ) {
        return MIMEtype;
    }
}
