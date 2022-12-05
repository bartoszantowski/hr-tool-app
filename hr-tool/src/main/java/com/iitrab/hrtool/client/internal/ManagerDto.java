package com.iitrab.hrtool.client.internal;

import org.eclipse.jdt.annotation.Nullable;

record ManagerDto(@Nullable Long id,
                  String name,
                  String lastName,
                  String email) {
}
