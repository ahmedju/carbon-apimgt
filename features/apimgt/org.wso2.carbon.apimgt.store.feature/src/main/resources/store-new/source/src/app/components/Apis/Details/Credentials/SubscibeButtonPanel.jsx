/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import { Typography } from '@material-ui/core';
import Button from '@material-ui/core/Button';
import { ScopeValidation, resourceMethods, resourcePaths } from 'AppComponents/Shared/ScopeValidation';
import PropTypes from 'prop-types';

const styles = theme => ({
    subscribeButtons: {
        display: 'flex',
        paddingTop: theme.spacing.unit * 2,
    },
    buttonElm: {
        height: 28,
        marginLeft: 20,
    },
    buttonElmText: {
        marginLeft: 20,
        paddingTop: 5,
    },
});
const subscibeButtonPanel = (props) => {
    const {
        classes, avalibleAppsLength, subscribedAppsLength, handleClickToggle,
    } = props;
    return (
        <div className={classes.subscribeButtons}>
            <div>
                <Typography variant='headline'>Subscribed Applications</Typography>
                <Typography variant='caption'>
                        (
                    {' '}
                    {subscribedAppsLength}
                    {' '}
                    {subscribedAppsLength === 1 ? 'subscription' : 'subscriptions'}
                    {' '}
                        )
                </Typography>
            </div>
            <ScopeValidation
                resourcePath={resourcePaths.SUBSCRIPTIONS}
                resourceMethod={resourceMethods.POST}
            >
                {avalibleAppsLength > 0 && (
                    <div>
                        <Button
                            variant='outlined'
                            size='small'
                            color='primary'
                            className={classes.buttonElm}
                            onClick={() => handleClickToggle('openAvailable')}
                        >
                            Subscribe to Available App
                        </Button>
                        <Typography
                            variant='caption'
                            component='p'
                            className={classes.buttonElmText}
                        >
                            {avalibleAppsLength}
                            {' '}
                            Available
                        </Typography>
                    </div>
                )}
                <Button
                    variant='outlined'
                    size='small'
                    color='primary'
                    className={classes.buttonElm}
                    onClick={() => handleClickToggle('openNew')}
                >
                    Subscribe to New App
                </Button>
            </ScopeValidation>
        </div>
    );
};
subscibeButtonPanel.propTypes = {
    classes: PropTypes.shape({
        subscribeButtons: PropTypes.shape({}),
        buttonElm: PropTypes.shape({}),
        buttonElmText: PropTypes.shape({}),
    }).isRequired,
    handleClickToggle: PropTypes.func.isRequired,
    avalibleAppsLength: PropTypes.number.isRequired,
    subscribedAppsLength: PropTypes.number.isRequired,
};
export default withStyles(styles, { withTheme: true })(subscibeButtonPanel);
