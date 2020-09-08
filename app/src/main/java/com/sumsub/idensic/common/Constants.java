package com.sumsub.idensic.common;

import com.sumsub.idensic.model.DocSet;
import com.sumsub.idensic.model.DocSetType;
import com.sumsub.idensic.model.DocSubType;
import com.sumsub.idensic.model.DocType;

import java.util.Arrays;
import java.util.Collections;

public interface Constants {

    String USER_ID = "msdk2-demo-android-%s";

    DocSet identity = new DocSet(
            DocSetType.IDENTITY,
            Arrays.asList(DocType.ID_CARD, DocType.PASSPORT, DocType.DRIVERS),
            Arrays.asList(DocSubType.FRONT_SIDE, DocSubType.BACK_SIDE)
    );

    DocSet selfie = new DocSet(
            DocSetType.SELFIE,
            Collections.singletonList(DocType.SELFIE),
            null
    );

    DocSet proofOfResidence = new DocSet(
            DocSetType.PROOF_OF_RESIDENCE,
            Collections.singletonList(DocType.UTILITY_BILL),
            null
    );

}
