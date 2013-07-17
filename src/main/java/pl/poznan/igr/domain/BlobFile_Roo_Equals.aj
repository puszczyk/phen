// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package pl.poznan.igr.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.poznan.igr.domain.BlobFile;

privileged aspect BlobFile_Roo_Equals {
    
    public boolean BlobFile.equals(Object obj) {
        if (!(obj instanceof BlobFile)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        BlobFile rhs = (BlobFile) obj;
        return new EqualsBuilder().append(contentType, rhs.contentType).append(created, rhs.created).append(fileName, rhs.fileName).append(id, rhs.id).isEquals();
    }
    
    public int BlobFile.hashCode() {
        return new HashCodeBuilder().append(contentType).append(created).append(fileName).append(id).toHashCode();
    }
    
}