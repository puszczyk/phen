// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package pl.poznan.igr.domain;

import java.util.Date;
import pl.poznan.igr.domain.BlobFile;
import pl.poznan.igr.domain.Context;
import pl.poznan.igr.domain.ZipSession;

privileged aspect ZipSession_Roo_JavaBean {
    
    public Date ZipSession.getCreationDate() {
        return this.creationDate;
    }
    
    public void ZipSession.setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Context ZipSession.getContext() {
        return this.context;
    }
    
    public void ZipSession.setContext(Context context) {
        this.context = context;
    }
    
    public BlobFile ZipSession.getBlobFileEnriched() {
        return this.blobFileEnriched;
    }
    
    public void ZipSession.setBlobFileEnriched(BlobFile blobFileEnriched) {
        this.blobFileEnriched = blobFileEnriched;
    }
    
    public BlobFile ZipSession.getBlobFileReduced() {
        return this.blobFileReduced;
    }
    
    public void ZipSession.setBlobFileReduced(BlobFile blobFileReduced) {
        this.blobFileReduced = blobFileReduced;
    }
    
}
