/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.xerces.xs.datatypes;

import org.apache.xerces.xs.XSException;

/**
 * <p><b>EXPERIMENTAL: This interface should not be considered stable.
 * It is likely it may be altered or replaced in the future.</b></p>
 * 
 * <p>The <code>ByteList</code> is an immutable ordered collection of 
 * <code>byte</code>.</p>
 * 
 * @author Ankit Pasricha, IBM
 * 
 * @version $Id$
 */
public interface ByteList {
    
    /**
     * The number of <code>byte</code>s in the list. The range of 
     * valid child object indices is 0 to <code>length-1</code> inclusive. 
     */
    public int getLength();
    
    /**
     * Checks if the <code>byte</code> <code>item</code> is a 
     * member of this list. 
     * @param item  <code>byte</code> whose presence in this list 
     *   is to be tested. 
     * @return  True if this list contains the <code>byte</code> 
     *   <code>item</code>. 
     */
    public boolean contains(byte item);
    
    /**
     * Returns the <code>index</code>th item in the collection. The index 
     * starts at 0. 
     * @param index  index into the collection. 
     * @return  The <code>byte</code> at the <code>index</code>th 
     *   position in the <code>ByteList</code>. 
     * @exception XSException
     *   INDEX_SIZE_ERR: if <code>index</code> is greater than or equal to the 
     *   number of objects in the list.
     */
    public byte item(int index) throws XSException;
    
}
