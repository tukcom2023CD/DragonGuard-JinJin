package com.dragonguard.backend.global;

import org.hibernate.annotations.Where;

@Where(clause = "deleted_at is null")
public @interface SoftDelete {}
