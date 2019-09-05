package com.cib.edip.edipsftpserver.sftpd;

import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.subsystem.sftp.SftpEventListener;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystem;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;

public class CustomSftpSubsystemFactory extends SftpSubsystemFactory {
    @Override
    public Command create() {
        final SftpSubsystem subsystem = new SftpSubsystem(getExecutorService(),
                getUnsupportedAttributePolicy(),getFileSystemAccessor(),getErrorStatusDataHandler()) {
            @Override
            protected void setFileAttribute(final Path file, final String view, final String attribute,
                                            final Object value, final LinkOption... options) throws IOException {
                throw new UnsupportedOperationException("setFileAttribute Disabled");
            }

            @Override
            protected void createLink(final int id, final String targetPath, final String linkPath,
                                      final boolean symLink) throws IOException {
                throw new UnsupportedOperationException("createLink Disabled");
            }
        };
        final Collection<? extends SftpEventListener> listeners = getRegisteredListeners();
        if (GenericUtils.size(listeners) > 0) {
            for (final SftpEventListener l : listeners) {
                subsystem.addSftpEventListener(l);
            }
        }

        //subsystem.setFileSystem();
        return subsystem;
    }
}