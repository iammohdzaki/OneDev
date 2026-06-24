package com.one.dev.presentation.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val GithubIcon: ImageVector
    get() = ImageVector.Builder(
        name = "GithubIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.White)
    ) {
        moveTo(12.0f, 2.0f)
        curveTo(6.477f, 2.0f, 2.0f, 6.484f, 2.0f, 12.017f)
        curveTo(2.0f, 16.446f, 4.87f, 20.199f, 8.852f, 21.528f)
        curveTo(9.352f, 21.622f, 9.534f, 21.312f, 9.534f, 21.046f)
        curveTo(9.534f, 21.046f, 9.534f, 21.046f, 9.534f, 21.046f) // Keep exactly identical
        curveTo(9.534f, 20.812f, 9.525f, 20.192f, 9.52f, 19.367f)
        curveTo(6.738f, 19.972f, 6.15f, 18.03f, 6.15f, 18.03f)
        curveTo(5.696f, 16.877f, 5.038f, 16.57f, 5.038f, 16.57f)
        curveTo(4.13f, 15.946f, 5.107f, 15.958f, 5.107f, 15.958f)
        curveTo(6.111f, 16.029f, 6.64f, 16.992f, 6.64f, 16.992f)
        curveTo(7.533f, 18.527f, 8.984f, 18.082f, 9.554f, 17.828f)
        curveTo(9.645f, 17.178f, 9.905f, 16.737f, 10.192f, 16.485f)
        curveTo(7.971f, 16.231f, 5.636f, 15.369f, 5.636f, 11.517f)
        curveTo(5.636f, 10.42f, 6.026f, 9.522f, 6.663f, 8.82f)
        curveTo(6.56f, 8.565f, 6.218f, 7.544f, 6.762f, 6.162f)
        curveTo(6.762f, 6.162f, 7.604f, 5.891f, 9.516f, 7.19f)
        curveTo(10.317f, 6.966f, 11.173f, 6.854f, 12.022f, 6.85f)
        curveTo(12.87f, 6.854f, 13.727f, 6.966f, 14.53f, 7.19f)
        curveTo(16.44f, 5.891f, 17.28f, 6.162f, 17.28f, 6.162f)
        curveTo(17.826f, 7.544f, 17.483f, 8.565f, 17.382f, 8.82f)
        curveTo(18.021f, 9.522f, 18.407f, 10.42f, 18.407f, 11.517f)
        curveTo(18.407f, 15.378f, 16.07f, 16.228f, 13.842f, 16.477f)
        curveTo(14.2f, 16.786f, 14.518f, 17.399f, 14.518f, 18.336f)
        curveTo(14.518f, 19.679f, 14.507f, 20.763f, 14.507f, 21.046f)
        curveTo(14.507f, 21.315f, 14.685f, 21.628f, 15.195f, 21.528f)
        curveTo(19.173f, 20.194f, 22.0f, 16.443f, 22.0f, 12.017f)
        curveTo(22.0f, 6.484f, 17.522f, 2.0f, 12.0f, 2.0f)
        close()
    }.build()

val LinkedInIcon: ImageVector
    get() = ImageVector.Builder(
        name = "LinkedInIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.White)
    ) {
        moveTo(19.0f, 3.0f)
        lineTo(5.0f, 3.0f)
        curveTo(3.9f, 3.0f, 3.0f, 3.9f, 3.0f, 5.0f)
        lineTo(3.0f, 19.0f)
        curveTo(3.0f, 20.1f, 3.9f, 21.0f, 5.0f, 21.0f)
        lineTo(19.0f, 21.0f)
        curveTo(20.1f, 21.0f, 21.0f, 20.1f, 21.0f, 19.0f)
        lineTo(21.0f, 5.0f)
        curveTo(21.0f, 3.9f, 20.1f, 3.0f, 19.0f, 3.0f)
        close()
        moveTo(9.0f, 17.0f)
        lineTo(6.5f, 17.0f)
        lineTo(6.5f, 9.5f)
        lineTo(9.0f, 9.5f)
        lineTo(9.0f, 17.0f)
        close()
        moveTo(7.75f, 8.25f)
        curveTo(6.92f, 8.25f, 6.25f, 7.58f, 6.25f, 6.75f)
        curveTo(6.25f, 5.92f, 6.92f, 5.25f, 7.75f, 5.25f)
        curveTo(8.58f, 5.25f, 9.25f, 5.92f, 9.25f, 6.75f)
        curveTo(9.25f, 7.58f, 8.58f, 8.25f, 7.75f, 8.25f)
        close()
        moveTo(18.0f, 17.0f)
        lineTo(15.5f, 17.0f)
        lineTo(15.5f, 13.0f)
        curveTo(15.5f, 12.0f, 15.0f, 11.5f, 14.25f, 11.5f)
        curveTo(13.5f, 11.5f, 13.0f, 12.0f, 13.0f, 13.0f)
        lineTo(13.0f, 17.0f)
        lineTo(10.5f, 17.0f)
        lineTo(10.5f, 9.5f)
        lineTo(13.0f, 9.5f)
        lineTo(13.0f, 10.5f)
        curveTo(13.5f, 9.75f, 14.25f, 9.25f, 15.25f, 9.25f)
        curveTo(16.75f, 9.25f, 18.0f, 10.25f, 18.0f, 12.25f)
        lineTo(18.0f, 17.0f)
        close()
    }.build()

val DiscordIcon: ImageVector
    get() = ImageVector.Builder(
        name = "DiscordIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        fill = SolidColor(Color.White)
    ) {
        moveTo(18.966f, 6.093f)
        curveTo(17.525f, 5.437f, 15.986f, 4.962f, 14.373f, 4.697f)
        curveTo(14.341f, 4.692f, 14.309f, 4.707f, 14.292f, 4.737f)
        curveTo(14.093f, 5.093f, 13.872f, 5.56f, 13.717f, 5.925f)
        curveTo(11.996f, 5.667f, 10.287f, 5.667f, 8.59f, 5.925f)
        curveTo(8.435f, 5.56f, 8.207f, 5.093f, 8.008f, 4.737f)
        curveTo(7.991f, 4.707f, 7.959f, 4.692f, 7.927f, 4.697f)
        curveTo(6.312f, 4.962f, 4.771f, 5.437f, 3.329f, 6.093f)
        curveTo(3.316f, 6.099f, 3.305f, 6.109f, 3.299f, 6.122f)
        curveTo(0.38f, 10.485f, -0.421f, 14.743f, 0.165f, 18.937f)
        curveTo(0.168f, 18.956f, 0.178f, 18.974f, 0.193f, 18.986f)
        curveTo(2.115f, 20.398f, 3.978f, 21.256f, 5.807f, 21.822f)
        curveTo(5.84f, 21.832f, 5.875f, 21.82f, 5.895f, 21.791f)
        curveTo(6.326f, 21.303f, 6.709f, 20.781f, 7.042f, 20.228f)
        curveTo(7.063f, 20.193f, 7.046f, 20.149f, 7.01f, 20.136f)
        curveTo(6.398f, 19.903f, 5.817f, 19.619f, 5.263f, 19.29f)
        curveTo(5.223f, 19.266f, 5.221f, 19.208f, 5.259f, 19.181f)
        curveTo(5.375f, 19.094f, 5.489f, 19.003f, 5.599f, 18.91f)
        curveTo(5.618f, 18.894f, 5.644f, 18.89f, 5.666f, 18.899f)
        curveTo(9.512f, 20.662f, 13.682f, 20.662f, 17.478f, 18.899f)
        curveTo(17.5f, 18.89f, 17.526f, 18.894f, 17.545f, 18.91f)
        curveTo(17.655f, 19.003f, 17.769f, 19.094f, 17.885f, 19.181f)
        curveTo(17.923f, 19.208f, 17.921f, 19.266f, 17.881f, 19.29f)
        curveTo(17.327f, 19.619f, 16.746f, 19.903f, 16.134f, 20.136f)
        curveTo(16.098f, 20.149f, 16.081f, 20.193f, 16.102f, 20.228f)
        curveTo(16.444f, 20.781f, 16.827f, 21.303f, 17.249f, 21.791f)
        curveTo(17.269f, 21.82f, 17.304f, 21.832f, 17.337f, 21.822f)
        curveTo(19.175f, 21.256f, 21.038f, 20.398f, 22.96f, 18.986f)
        curveTo(22.975f, 18.974f, 22.985f, 18.956f, 22.988f, 18.937f)
        curveTo(23.642f, 13.974f, 21.884f, 9.77f, 18.995f, 6.122f)
        curveTo(18.99f, 6.109f, 18.979f, 6.099f, 18.966f, 6.093f)
        close()
        moveTo(8.006f, 15.932f)
        curveTo(6.9f, 15.932f, 5.992f, 14.912f, 5.992f, 13.666f)
        curveTo(5.992f, 12.42f, 6.88f, 11.4f, 8.006f, 11.4f)
        curveTo(9.141f, 11.4f, 10.039f, 12.42f, 10.019f, 13.666f)
        curveTo(10.019f, 14.912f, 9.131f, 15.932f, 8.006f, 15.932f)
        close()
        moveTo(16.012f, 15.932f)
        curveTo(14.906f, 15.932f, 13.998f, 14.912f, 13.998f, 13.666f)
        curveTo(13.998f, 12.42f, 14.886f, 11.4f, 16.012f, 11.4f)
        curveTo(17.147f, 11.4f, 18.045f, 12.42f, 18.025f, 13.666f)
        curveTo(18.025f, 14.912f, 17.137f, 15.932f, 16.012f, 15.932f)
        close()
    }.build()
