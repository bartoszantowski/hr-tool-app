package com.iitrab.hrtool.client.internal;

import org.eclipse.jdt.annotation.Nullable;

record ClientDto(@Nullable Long id,
                 String name,
                 @Nullable String contactNumber,
                 @Nullable String contactEmail,
                 @Nullable ManagerDto manager) {
}
