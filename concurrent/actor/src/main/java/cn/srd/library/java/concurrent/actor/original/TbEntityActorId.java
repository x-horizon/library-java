/**
 * Copyright © 2016-2024 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.srd.library.java.concurrent.actor.original;

import lombok.Getter;

import java.util.Objects;

public class TbEntityActorId implements TbActorId {

    @Getter
    private final EntityId entityId;

    public TbEntityActorId(EntityId entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return entityId.getEntityType() + "|" + entityId.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbEntityActorId that = (TbEntityActorId) o;
        return entityId.equals(that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }

    @Override
    public EntityType getEntityType() {
        return entityId.getEntityType();
    }

}